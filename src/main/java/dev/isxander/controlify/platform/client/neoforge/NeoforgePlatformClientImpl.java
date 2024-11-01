//? if forgelike {
package dev.isxander.controlify.platform.client.neoforge;

import dev.isxander.controlify.platform.client.CreativeTabHelper;
import dev.isxander.controlify.platform.client.PlatformClientUtilImpl;
import dev.isxander.controlify.platform.client.events.DisconnectedEvent;
import dev.isxander.controlify.platform.client.events.LifecycleEvent;
import dev.isxander.controlify.platform.client.events.ScreenRenderEvent;
import dev.isxander.controlify.platform.client.events.TickEvent;
import dev.isxander.controlify.platform.client.resource.ControlifyReloadListener;
import dev.isxander.controlify.platform.client.util.RenderLayer;
import dev.isxander.controlify.platform.neoforge.VanillaKeyMappingHolder;
import dev.isxander.controlify.platform.network.ControlifyPacketCodec;
import net.minecraft.SharedConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
//? if neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.GameShuttingDownEvent;
import net.neoforged.neoforgespi.language.IModInfo;
*///?} else {
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.GameShuttingDownEvent;
import net.minecraftforge.forgespi.language.IModInfo;
//?}
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class NeoforgePlatformClientImpl implements PlatformClientUtilImpl {
    private @Nullable Collection<KeyMapping> moddedKeyMappings;

    @Override
    public void registerClientTickStarted(TickEvent event) {
        //? if >=1.20.6 {
        /*NeoForge.EVENT_BUS.<ClientTickEvent.Pre>addListener(e -> {
        *///?} elif neoforge {
        /*NeoForge.EVENT_BUS.<net.neoforged.neoforge.event.TickEvent.ClientTickEvent>addListener(e -> {
            if (e.phase != net.neoforged.neoforge.event.TickEvent.Phase.START)
                return;
        *///?} else {
        MinecraftForge.EVENT_BUS.<net.minecraftforge.event.TickEvent.ClientTickEvent>addListener(e -> {
            if (e.phase != net.minecraftforge.event.TickEvent.Phase.START)
                return;
        //?}
            event.onTick(Minecraft.getInstance());
        });
    }

    @Override
    public void registerClientTickEnded(TickEvent event) {
        //? if >=1.20.6 {
        /*NeoForge.EVENT_BUS.<ClientTickEvent.Pre>addListener(e -> {
        *///?} elif neoforge {
        /*NeoForge.EVENT_BUS.<net.neoforged.neoforge.event.TickEvent.ClientTickEvent>addListener(e -> {
            if (e.phase != net.neoforged.neoforge.event.TickEvent.Phase.END)
                return;
        *///?} else {
        MinecraftForge.EVENT_BUS.<net.minecraftforge.event.TickEvent.ClientTickEvent>addListener(e -> {
            if (e.phase != net.minecraftforge.event.TickEvent.Phase.END)
                return;
        //?}
            event.onTick(Minecraft.getInstance());
        });
    }

    @Override
    public void registerClientStopping(LifecycleEvent event) {
        //? if neoforge {
        /*NeoForge.EVENT_BUS.<GameShuttingDownEvent>addListener(e -> {
        *///?} else {
        MinecraftForge.EVENT_BUS.<GameShuttingDownEvent>addListener(e -> {
        //?}
            event.onLifecycle(Minecraft.getInstance());
        });
    }

    @Override
    public void registerClientDisconnected(DisconnectedEvent event) {
        //? if neoforge {
        /*NeoForge.EVENT_BUS.<ClientPlayerNetworkEvent.LoggingOut>addListener(e -> {
        *///?} else {
        MinecraftForge.EVENT_BUS.<ClientPlayerNetworkEvent.LoggingOut>addListener(e -> {
        //?}
            event.onDisconnected(Minecraft.getInstance());
        });
    }

    @Override
    public void registerAssetReloadListener(ControlifyReloadListener reloadListener) {
        getModEventBus().<RegisterClientReloadListenersEvent>addListener(e -> {
            e.registerReloadListener(reloadListener);
        });
    }

    @Override
    public void registerBuiltinResourcePack(ResourceLocation id, Component displayName) {
        ResourceLocation packLocation = id.withPrefix("resourcepacks/");

        getModEventBus().<AddPackFindersEvent>addListener(e -> {
            //? if >=1.20.6 {
            /*e.addPackFinders(
                    packLocation,
                    PackType.CLIENT_RESOURCES,
                    displayName,
                    PackSource.BUILT_IN,
                    false,
                    Pack.Position.TOP
            );
            *///?} else {
            IModInfo modInfo = ModList.get().getModContainerById(packLocation.getNamespace()).orElseThrow().getModInfo();
            Path resourcePath = modInfo.getOwningFile().getFile().findResource(packLocation.getPath());

            Pack pack = Pack.readMetaAndCreate(
                    packLocation.toString(),
                    displayName,
                    true,
                    //? if neoforge {
                    /*BuiltInPackSource.fromName((path) -> new PathPackResources(path, resourcePath, true)),
                    *///?} else {
                    (path) -> new PathPackResources(path, resourcePath, true),
                    //?}
                    PackType.CLIENT_RESOURCES,
                    Pack.Position.BOTTOM,
                    PackSource.BUILT_IN
            );
            e.addRepositorySource(consumer -> consumer.accept(pack));
            //?}
        });
    }

    @Override
    public void addHudLayer(ResourceLocation id, RenderLayer renderLayer) {
        //? if neoforge {
        /*getModEventBus().addListener(
                //? if >1.20.4 {
                /^RegisterGuiLayersEvent.class,
                ^///?} else {
                RegisterGuiOverlaysEvent.class,
                //?}
                e -> e.registerAboveAll(id, renderLayer)
        );
        *///?} else {
        getModEventBus().addListener((RegisterGuiOverlaysEvent e) -> e.registerAboveAll(id.getPath(), renderLayer));
        //?}
    }

    @Override
    public void registerPostScreenRender(ScreenRenderEvent event) {
        //? if neoforge {
        /*NeoForge.EVENT_BUS.<ScreenEvent.Render.Post>addListener(e -> {
        *///?} else {
        MinecraftForge.EVENT_BUS.<ScreenEvent.Render.Post>addListener(e -> {
        //?}
            event.onRender(e.getScreen(), e.getGuiGraphics(), e.getMouseX(), e.getMouseY(), e.getPartialTick());
        });
    }

    @Override
    public Collection<KeyMapping> getModdedKeyMappings() {
        if (moddedKeyMappings == null)
            moddedKeyMappings = calculateModdedKeyMappings();
        return moddedKeyMappings;
    }

    private Collection<KeyMapping> calculateModdedKeyMappings() {
        Options options = Minecraft.getInstance().options;
        KeyMapping[] vanillaAndModded = options.keyMappings;
        List<KeyMapping> vanillaOnly = Arrays.asList(((VanillaKeyMappingHolder) options).controlify$getVanillaKeys());

        return Arrays.stream(vanillaAndModded)
                .filter(key -> !vanillaOnly.contains(key))
                .toList();
    }

    @Override
    public <I, O> void setupClientsideHandshake(ResourceLocation handshakeId, ControlifyPacketCodec<I> clientBoundCodec, ControlifyPacketCodec<O> serverBoundCodec, Function<I, O> handshakeHandler) {
        // TODO
    }

    @Override
    public CreativeTabHelper createCreativeTabHelper(CreativeModeInventoryScreen creativeScreen) {
        return new NeoforgeCreativeTabHelper(creativeScreen);
    }

    private IEventBus getModEventBus() {
        //? if neoforge {
        /*return ModLoadingContext.get().getActiveContainer().getEventBus();
        *///?} else {
        return FMLJavaModLoadingContext.get().getModEventBus();
        //?}
    }
}
//?}
