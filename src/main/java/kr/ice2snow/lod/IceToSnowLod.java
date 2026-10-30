package kr.ice2snow.lod;

import com.seibel.distanthorizons.api.enums.rendering.EDhApiBlockMaterial;
import com.seibel.distanthorizons.api.methods.events.DhApiEventRegister;
import com.seibel.distanthorizons.api.methods.events.abstractEvents.DhApiBlockColorOverrideEvent;
import com.seibel.distanthorizons.api.methods.events.abstractEvents.DhApiBlockStateWrapperCreatedEvent;
import com.seibel.distanthorizons.api.methods.events.sharedParameterObjects.DhApiEventParam;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public final class IceToSnowLod implements ModInitializer {
    public static final String MOD_ID = "ice_to_snow_lod";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Vanilla snow-block map color is approximately RGB(249, 255, 254).
    private static final int SNOW_R = 249;
    private static final int SNOW_G = 255;
    private static final int SNOW_B = 254;

    @Override
    public void onInitialize() {
        DhApiEventRegister.on(
            DhApiBlockStateWrapperCreatedEvent.class,
            new DhApiBlockStateWrapperCreatedEvent() {
                @Override
                public void blockStateWrapperCreated(
                    DhApiEventParam<EventParam> event
                ) {
                    String id = serialId(event.value.getBlockStateWrapper().getSerialString());
                    if (!isIce(id)) return;

                    // Convert the DH material classification to snow. This affects DH's
                    // LOD representation only; the Minecraft world BlockState is untouched.
                    event.value.setBlockMaterial(EDhApiBlockMaterial.SNOW);
                    event.value.setOpacity(15);
                    // Explicitly permit the color override callback for this block.
                    event.value.setAllowApiColorOverride(true);
                }
            }
        );

        DhApiEventRegister.on(
            DhApiBlockColorOverrideEvent.class,
            new DhApiBlockColorOverrideEvent() {
                @Override
                public void onBlockColorOverridden(
                    DhApiEventParam<EventParam> event
                ) {
                    String id = serialId(event.value.getBlockStateWrapper().getSerialString());
                    if (!isIce(id)) return;
                    event.value.setColor(SNOW_R, SNOW_G, SNOW_B);
                }
            }
        );

        LOGGER.info("Ice to Snow LOD loaded. Ice-family DH materials will be represented as snow-colored snow material.");
    }

    private static String serialId(String serial) {
        if (serial == null) return "";
        String s = serial.toLowerCase(Locale.ROOT);
        // DH serial strings may include state properties after the resource id.
        int properties = s.indexOf('[');
        if (properties >= 0) s = s.substring(0, properties);
        int separator = s.indexOf(' ');
        if (separator >= 0) s = s.substring(0, separator);
        return s.trim();
    }

    private static boolean isIce(String id) {
        return id.equals("minecraft:ice")
            || id.equals("minecraft:packed_ice")
            || id.equals("minecraft:blue_ice")
            || id.equals("minecraft:frosted_ice");
    }
}
