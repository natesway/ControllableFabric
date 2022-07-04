package com.mrcrayfish.controllable;

import com.google.common.io.ByteStreams;
import com.jab125.event.impl.ScreenRenderBeforeEvent;
import com.jab125.event.impl.TickEvent;
import com.jab125.impl.ControllableJarFinder;
import com.jab125.impl.EarlyControllableSetup;
import com.jab125.thonkutil.api.annotations.SubscribeEvent;
import com.jab125.thonkutil.api.events.EventTaxi;
import com.jab125.thonkutil.api.events.client.screen.ScreenEvent;
import com.jab125.thonkutil.api.events.client.screen.ScreenRenderEvent;
import com.jab125.thonkutil.api.events.client.screen.TitleScreenEvent;
import com.jab125.thonkutil.api.events.client.screen.TitleScreenRenderEvent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.controllable.client.*;
import com.mrcrayfish.controllable.client.gui.screens.ButtonBindingScreen;
import com.mrcrayfish.controllable.client.gui.screens.ControllerLayoutScreen;
import com.mrcrayfish.controllable.client.gui.screens.ControllerSelectionScreen;
import com.mrcrayfish.controllable.integration.ModMenuCompat;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.ModContainerImpl;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.Optional;

/**
 * Author: MrCrayfish
 */
public class Controllable implements ClientModInitializer, ModInitializer
{
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);

    private static ControllerManager manager;

    public static ControllerManager getManagerInternal() {
        if (StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass() == ModMenuCompat.class) {
            return manager;
        }
        throw new IllegalStateException("NO");
    }
    public static int getConnectedControllers() {
        return manager.getControllerCount();
    }

    public static Controller controller;
    private static ControllerInput input;
    private static File configFolder;
    private static boolean jeiLoaded;

//    public Controllable()
//    {
//        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
//        ModLoadingContext.registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);
//        ModLoadingContext.registerConfig(ModConfig.Type.SERVER, Config.serverSpec);
//        //Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
//        //ModLoadingContext.registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
//    }

    @Environment(EnvType.CLIENT)
    public static class CannotFindControllable {
        private static boolean hasPoppedUp = false;
        @SubscribeEvent
        public static void render(TitleScreenRenderEvent a) {
            System.out.println(a);
            if (!hasPoppedUp) {
                Minecraft.getInstance().getToasts().addToast(new CannotFindControllableToast());
                hasPoppedUp = true;
            }
        }
        public static class CannotFindControllableToast implements Toast {
            @Override
            public Visibility render(PoseStack poseStack, ToastComponent toastComponent, long delta)
            {
                RenderSystem.setShaderTexture(0, TEXTURE);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                toastComponent.blit(poseStack, 0, 0, 0, 32, 160, 32);

                //RenderSystem.setShaderTexture(0, ControllerLayoutScreen.TEXTURE);
                //RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                //toastComponent.blit(poseStack, 8, 8, 20, 43, 20, 16);

                String title = toastComponent.getMinecraft().font.plainSubstrByWidth("Controllable Jar Not Found!", 140);
                toastComponent.getMinecraft().font.draw(poseStack, title, 5, 7, 0);

                Component message = Component.literal("Download the Forge mod jar");
                toastComponent.getMinecraft().font.draw(poseStack, message, 5, 18, 0);

                return delta >= 3000000000L ? Visibility.HIDE : Visibility.SHOW;
            }
        }
    }
    private static boolean a = false;
    @Override
    public void onInitializeClient() {
        if (EarlyControllableSetup.controllableVersion == null) {
            EventTaxi.registerEventTaxiSubscriber(CannotFindControllable.class);
            return;
        }
        try {
            System.out.println(ControllableJarFinder.findJar());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //System.exit(0);
        }
        //((ModContainerImpl)FabricLoader.getInstance().getModContainer("controllable").get()).getRootPaths();
        net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
                    net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.beforeRender(screen).register((screen1, matrices, mouseX, mouseY, tickDelta) -> {
                        EventTaxi.executeEventTaxi(new ScreenRenderBeforeEvent(screen, matrices, mouseX, mouseY, tickDelta));
                    });
                //});
            if (a) return;
            a = true;
        ModLoadingContext.registerConfig("controllable", ModConfig.Type.CLIENT, Config.clientSpec);
        Minecraft mc = Minecraft.getInstance();
        configFolder = new File(mc.gameDirectory, "config");
        jeiLoaded = FabricLoader.getInstance().isModLoaded("jei");

        ControllerProperties.load(configFolder);
            //https://raw.githubusercontent.com/MrCrayfish/Controllable/e25cefd21f0a85c7c4a34ed790e333d12a5adf91/src/main/resources/mappings/defender_game_racer_x7.json
        try(InputStream is = EarlyControllableSetup.gameControllerDb)
        {
            if(is != null)
            {
                byte[] bytes = ByteStreams.toByteArray(is);
                ByteBuffer buffer = MemoryUtil.memASCIISafe(new String(bytes));
                //System.out.println(new String(bytes));
                if(buffer != null && GLFW.glfwUpdateGamepadMappings(buffer))
                {
                    LOGGER.info("Successfully updated gamepad mappings");
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        /* Loads up the controller manager and adds a listener */
        Controllable.manager = new ControllerManager();
        Controllable.manager.addControllerListener(new IControllerListener()
        {
            @Override
            public void connected(int jid)
            {
                Minecraft.getInstance().doRunTask(() ->
                {
                    if(Controllable.controller == null)
                    {
                        if(Config.CLIENT.options.autoSelect.get())
                        {
                            setController(new Controller(jid));
                        }

                        Minecraft mc = Minecraft.getInstance();
                        if(mc.player != null && Controllable.controller != null)
                        {
                            Minecraft.getInstance().getToasts().addToast(new ControllerToast(true, Controllable.controller.getName()));
                        }
                    }
                });
            }

            @Override
            public void disconnected(int jid)
            {
                Minecraft.getInstance().doRunTask(() ->
                {
                    if(Controllable.controller != null)
                    {
                        if(Controllable.controller.getJid() == jid)
                        {
                            Controller oldController = Controllable.controller;

                            setController(null);

                            if(Config.CLIENT.options.autoSelect.get() && manager.getControllerCount() > 0)
                            {
                                Optional<Integer> optional = manager.getControllers().keySet().stream().min(Comparator.comparing(i -> i));
                                optional.ifPresent(minJid -> setController(new Controller(minJid)));
                            }

                            Minecraft mc = Minecraft.getInstance();
                            if(mc.player != null)
                            {
                                Minecraft.getInstance().getToasts().addToast(new ControllerToast(false, oldController.getName()));
                            }
                        }
                    }
                });
            }
        });

        /* Attempts to load the first controller connected if auto select is enabled */
        if(Config.CLIENT.options.autoSelect.get())
        {
            if(GLFW.glfwJoystickPresent(GLFW.GLFW_JOYSTICK_1) && GLFW.glfwJoystickIsGamepad(GLFW.GLFW_JOYSTICK_1))
            {
                setController(new Controller(GLFW.GLFW_JOYSTICK_1));
            }
        }

            input = new ControllerInput();
        Mappings.load(configFolder);
            BindingRegistry.getInstance().load();

            EventTaxi.registerEventTaxiSubscriber(new ScreenEvents(Controllable.manager));
            EventTaxi.registerEventTaxiSubscriber(RadialMenuHandler.instance());
            EventTaxi.registerEventTaxiSubscriber(this);
            EventTaxi.registerEventTaxiSubscriber(input);
            EventTaxi.registerEventTaxiSubscriber(new RenderEvents());
            EventTaxi.registerEventTaxiSubscriber(new ControllerEvents());
        /* Registers events */
//        MinecraftForge.EVENT_BUS.register(new ControllerEvents());
    });
    }

    @Nullable
    public static Controller getController()
    {
        return controller;
    }

    public static ControllerInput getInput()
    {
        return input;
    }

    public static File getConfigFolder()
    {
        return configFolder;
    }

    public static boolean isJeiLoaded()
    {
        return jeiLoaded;
    }

    public static void setController(@Nullable Controller controller)
    {
        if(controller != null)
        {
            Controllable.controller = controller;
            Mappings.updateControllerMappings(controller);
        }
        else
        {
            Controllable.controller = null;
        }
    }

    @SubscribeEvent
    public void controllerTick(TickEvent.ClientTickEvent event)
    {
        //System.out.println("TICK");
        if(event.phase != TickEvent.Phase.START)
            return;

        if(manager != null)
        {
            manager.update();
        }
        if(controller != null)
        {
            controller.updateGamepadState();
            gatherAndQueueControllerInput();
        }
    }
    
    private static void gatherAndQueueControllerInput()
    {
        Controller currentController = controller;
        if(currentController == null)
            return;
        ButtonStates states = new ButtonStates();
        states.setState(Buttons.A, getButtonState(GLFW.GLFW_GAMEPAD_BUTTON_A));
        states.setState(Buttons.B, getButtonState(GLFW.GLFW_GAMEPAD_BUTTON_B));
        states.setState(Buttons.X, getButtonState(GLFW.GLFW_GAMEPAD_BUTTON_X));
        states.setState(Buttons.Y, getButtonState(GLFW.GLFW_GAMEPAD_BUTTON_Y));
        states.setState(Buttons.SELECT, getButtonState(GLFW.GLFW_GAMEPAD_BUTTON_BACK));
        states.setState(Buttons.HOME, getButtonState(GLFW.GLFW_GAMEPAD_BUTTON_GUIDE));
        states.setState(Buttons.START, getButtonState(GLFW.GLFW_GAMEPAD_BUTTON_START));
        states.setState(Buttons.LEFT_THUMB_STICK, getButtonState(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB));
        states.setState(Buttons.RIGHT_THUMB_STICK, getButtonState(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB));
        states.setState(Buttons.LEFT_BUMPER, getButtonState(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER));
        states.setState(Buttons.RIGHT_BUMPER, getButtonState(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER));
        states.setState(Buttons.LEFT_TRIGGER, currentController.getLTriggerValue() >= 0.5F);
        states.setState(Buttons.RIGHT_TRIGGER, currentController.getRTriggerValue() >= 0.5F);
        states.setState(Buttons.DPAD_UP, getButtonState(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP));
        states.setState(Buttons.DPAD_DOWN, getButtonState(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN));
        states.setState(Buttons.DPAD_LEFT, getButtonState(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT));
        states.setState(Buttons.DPAD_RIGHT, getButtonState(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT));
        processButtonStates(states);
    }

    private static void processButtonStates(ButtonStates states)
    {
        ButtonBinding.tick();
        for(int i = 0; i < Buttons.BUTTONS.length; i++)
        {
            processButton(Buttons.BUTTONS[i], states);
        }
    }

    private static void processButton(int index, ButtonStates newStates)
    {
        boolean state = newStates.getState(index);

        Screen screen = Minecraft.getInstance().screen;
        if(screen instanceof ControllerLayoutScreen)
        {
            ((ControllerLayoutScreen) screen).processButton(index, newStates);
            return;
        }

        if (controller == null)
        {
            return;
        }

        if(controller.getMapping() != null)
        {
            index = controller.getMapping().remap(index);
        }

        //No binding so don't perform any action
        if(index == -1)
        {
            return;
        }

        ButtonStates states = controller.getButtonsStates();

        if(state)
        {
            if(!states.getState(index))
            {
                states.setState(index, true);
                if(screen instanceof ButtonBindingScreen)
                {
                    if(((ButtonBindingScreen) screen).processButton(index))
                    {
                        return;
                    }
                }
                input.handleButtonInput(controller, index, true, false);
            }
        }
        else if(states.getState(index))
        {
            states.setState(index, false);
            input.handleButtonInput(controller, index, false, false);
        }
    }

    /**
     * Returns whether a button on the controller is pressed or not. This is a raw approach to
     * getting whether a button is pressed or not. You should use a {@link ButtonBinding} instead.
     *
     * @param button the button to check if pressed
     * @return
     */
    public static boolean isButtonPressed(int button)
    {
        return controller != null && controller.getButtonsStates().getState(button);
    }

    private static boolean getButtonState(int buttonCode)
    {
        return controller != null && controller.getGamepadState().buttons(buttonCode) == GLFW.GLFW_PRESS;
    }

    @Override
    public void onInitialize() {
        if (EarlyControllableSetup.controllableVersion == null) {
            return;
        }
        ModLoadingContext.registerConfig("controllable", ModConfig.Type.SERVER, Config.serverSpec);
    }
}
