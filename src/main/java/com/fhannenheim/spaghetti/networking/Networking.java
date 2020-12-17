package com.fhannenheim.spaghetti.networking;
import com.fhannenheim.spaghetti.Spaghetti;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {

    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    private static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Spaghetti.MOD_ID, "boots"),
                () -> Spaghetti.MOD_VER,
                s -> true,
                s -> true);

        INSTANCE.messageBuilder(SyncSpaghettiWithClient.class,nextID())
                .encoder(SyncSpaghettiWithClient::encode)
                .decoder(SyncSpaghettiWithClient::new)
                .consumer(SyncSpaghettiWithClient::handle)
                .add();

        INSTANCE.messageBuilder(EatSpaghetti.class,nextID())
                .encoder(EatSpaghetti::encode)
                .decoder(EatSpaghetti::new)
                .consumer(EatSpaghetti::handle)
                .add();
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }
}