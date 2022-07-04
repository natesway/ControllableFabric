package com.jab125.impl;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.devtech.arrp.ARRP;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.lang.JLang;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.impl.resource.loader.FabricModResourcePack;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.impl.ModContainerImpl;
import net.fabricmc.loader.impl.discovery.ModCandidate;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class EarlyControllableSetup implements PreLaunchEntrypoint {
    public static InputStream internalMappings;
    public static InputStream gameControllerDb;
    public static InputStream controllableLogo;
    public static String controllableVersion;
    public static String controllableDesc;
    @Override
    public void onPreLaunch() { // this is how you create chaos
        try {
            var qd = RuntimeResourcePack.create("controllable");
            var d = ControllableJarFinder.findJar();
            var l = new JarFile(d);
            var q = l.entries();
            while (q.hasMoreElements()) {
                ZipEntry entry = q.nextElement();
                InputStream inputStream = l.getInputStream(entry);
                //System.out.println(
                //new String(inputStream.readAllBytes())
                //        );
                if (entry.getName().contains("mappings")) {
                    System.out.println(entry);
                    internalMappings = l.getInputStream(entry);
                }
                var a = entry.getName();
                if (a.equals("controllable_icon.png")) {
                    controllableLogo = l.getInputStream(entry);
                }
                if (a.equals("gamecontrollerdb.txt")) {
                    gameControllerDb = l.getInputStream(entry);
                }
                if (a.equals("META-INF/mods.toml")) {
                    var dd = TomlFormat.instance().createParser().parse(l.getInputStream(entry));
                    var f = (ArrayList<UnmodifiableConfig>) dd.get("mods");
                    controllableVersion = (f.get(0).get("version").toString());
                    controllableDesc = (f.get(0).get("description").toString());
                }
                if (entry.getName().contains("assets")) {
                    System.out.println(entry);
                    if (a.contains("assets/controllable/lang/")) {
                        a = a.replace("assets/controllable/lang/", "");
                        a = a.replace(".json", "");
                        qd.addAsset(fix(id(a), "lang", "json"), l.getInputStream(entry).readAllBytes());
                    }
                    if (a.contains("assets/controllable/textures/gui")) {
                        qd.addAsset(id("textures/gui/" + a.replace("assets/controllable/textures/gui/", "")), l.getInputStream(entry).readAllBytes());
                    }
                }
                //qd.addAsset(new ResourceLocation(entry.getName()), inputStream.readAllBytes());
            }
            RRPCallback.BEFORE_VANILLA.register(resources -> {
                        resources.add(qd);
                    });
            System.out.println(Arrays.toString(d.listFiles()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.exit(0);
    }
    private static ResourceLocation id(String s) {
        return new ResourceLocation("controllable", s);
    }

    private static ResourceLocation fix(ResourceLocation identifier, String prefix, String append) {
        return new ResourceLocation(identifier.getNamespace(), prefix + '/' + identifier.getPath() + '.' + append);
    }
}
