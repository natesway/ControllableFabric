package com.jab125.impl;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipError;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ControllableJarFinder {
    public static JarType jarType;

    public static File findJar() throws IOException {
        File modsDir = new File(FabricLoader.getInstance().getGameDirectory(), "mods");
        File[] files = modsDir.listFiles();


        if (files != null) { // From optifabric
            File jar = null;

            for (File file : files) {
                if (!file.isDirectory() && "jar".equals(FilenameUtils.getExtension(file.getName())) && !file.getName().startsWith(".") && !file.isHidden()) {
                    JarType type = getJarType(file);
                    if (type.isError()) {
                        jarType = type;
                        throw new RuntimeException("An error occurred when trying to find the jar: " + type.name());
                    }

                    if (type == JarType.CONTROLLABLE) {
                        if (jar != null) {
                            jarType = JarType.DUPLICATED;
                            throw new FileAlreadyExistsException("Multiple jars: " + file.getName() + " and " + jar.getName());
                        }

                        jarType = type;
                        jar = file;
                    }
                }
            }

            if (jar != null) {
                return jar;
            }
        }
        jarType = JarType.MISSING;
        throw new FileNotFoundException("Could not find controllable jar, get it at https://www.curseforge.com/minecraft/mc-mods/controllable");
    }


    private static JarType getJarType(File file) throws IOException {
        System.out.println(file);
        try (JarFile jarFile = new JarFile(file)) {
            JarEntry jarEntry = jarFile.getJarEntry("com/mrcrayfish/controllable/Controllable.class");
            if (jarEntry != null) {
                JarEntry ent2 = jarFile.getJarEntry("com/jab125/impl/ControllableJarFinder.class");
                if (ent2 != null) {
                    jarEntry = null;
                }
            }
            if (jarEntry == null) {
                return JarType.SOMETHING_ELSE;
            }
        } catch (ZipException | ZipError e) {
            return JarType.CORRUPT_ZIP;
        }

        return JarType.CONTROLLABLE;
    }

    public enum JarType {
        MISSING(true),
        CONTROLLABLE(false),
        INCOMPATIBLE(true),
        CORRUPT_ZIP(true),
        DUPLICATED(true),
        INTERNAL_ERROR(true),
        SOMETHING_ELSE(false);

        private final boolean error;

        JarType(boolean error) {
            this.error = error;
        }

        public boolean isError() {
            return error;
        }
    }
}
