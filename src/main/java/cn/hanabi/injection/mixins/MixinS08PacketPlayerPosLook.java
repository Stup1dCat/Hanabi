package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.IS08PacketPlayerPosLook;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(S08PacketPlayerPosLook.class)
public class MixinS08PacketPlayerPosLook implements IS08PacketPlayerPosLook {

    @Shadow
    private float yaw;

    @Shadow
    private float pitch;

    @Override
    public void setYaw(float y) {
        yaw = y;
    }

    @Override
    public void setPitch(float p) {
        pitch = p;
    }

}
