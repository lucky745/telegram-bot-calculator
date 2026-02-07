package com.lucky.bot.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucky.bot.part.Part;
import com.lucky.bot.part.grade.PartGrade;
import com.lucky.bot.part.grade.Upgrade;
import com.lucky.bot.part.type.Chassis;
import com.lucky.bot.part.type.Gadget;
import com.lucky.bot.part.type.PartType;
import com.lucky.bot.part.type.Weapon;
import com.lucky.bot.part.type.Wheel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Util {
    public static final int MAX_PART_LEVEL = 20;
    public static final String LOCALE_EN = "en";
    public static final String LOCALE_RU = "ru";
    public static final String LVL = "lvl";
    public static final String TYPE = "type";
    public static final String GRADE = "grade";
    public static final String MAX = "max";
    public static final String UP = "up";
    public static final String EMPTY_STRING = "";
    public static final String QUESTION_MARK = "❓";
    public static final String QUESTION = " ?";
    public static final String NEW_LINE = "\n";
    public static final String HTML = "HTML";
    public static final String MARKDOWN = "MarkdownV2";
    public static final String CONVERSATION_START_MESSAGE = "Choose language: ‼ Выберите язык:";
    public static final String IN_DEV = "in_dev";
    public static final String STAT_DIFF_PATTERN = "   ⋆ %s %,d (+%,d)%n";
    public static final String SINGLE_STAT_PATTERN = "   ⋆ %s %s%n";
    public static final String DOUBLE_STAT_PATTERN = "   ⋆ %s %s    %s %d%n";

    public static final List<Double> LEVEL_MULTIPLIERS = Arrays.asList(
            1.2,
            1.2,
            1.2,
            1.2,
            1.2,
            1.2,
            1.2,
            1.2,
            1.2,
            1.2,
            1.2,
            1.2,
            1.2,
            1.2,
            1.2,
            1.0874971688,
            1.0804573762,
            1.0744654566,
            1.069304654
    );

    public static final ObjectMapper objectMapper = new ObjectMapper();

    @Setter
    private static volatile MessageSource messageSource;

    public static String getLocalizedMessage(String key, String language) {
        MessageSource ms = messageSource;
        if (ms == null) {
            return key;
        }
        Locale locale = StringUtils.isEmpty(language) ? Locale.ENGLISH : Locale.forLanguageTag(language);
        return ms.getMessage(key, null, key, locale);
    }

    public static List<Part> parts = new ArrayList<>();

    static {
        int id = 0;
//--------------------------------------------------------------weapon---------------------------------------------------------------

        parts.add(new Part(id++, "ice_cream_mace", new Weapon(2000, 5), PartGrade.STANDARD));
        parts.add(new Part(id++, "hyperboloid", new Weapon(2500, 5), PartGrade.STANDARD));
        parts.add(new Part(id++, "mega_drill", new Weapon(3500, 5), PartGrade.STANDARD));
        parts.add(new Part(id++, "cutter_chainsaw", new Weapon(4000, 10), PartGrade.STANDARD));

        parts.add(new Part(id++, "santas_double_rocket", new Weapon(1750, 5), PartGrade.POLISHED));
        parts.add(new Part(id++, "swift_laser", new Weapon(2200, 5), PartGrade.POLISHED));
        parts.add(new Part(id++, "spike_strike", new Weapon(3500, 5), PartGrade.POLISHED));
        parts.add(new Part(id++, "big_boy", new Weapon(3500, 10), PartGrade.POLISHED));
        parts.add(new Part(id++, "dragon_mortar", new Weapon(2800, 15), PartGrade.POLISHED));
        parts.add(new Part(id++, "cutter_rocket", new Weapon(3000, 10), PartGrade.POLISHED));
        parts.add(new Part(id++, "dozer_double_blade", new Weapon(3500, 10), PartGrade.POLISHED));

        parts.add(new Part(id++, "blazing_mace", new Weapon(2500, 5), PartGrade.REFINED));
        parts.add(new Part(id++, "santas_laser_bell", new Weapon(3000, 10), PartGrade.REFINED));
        parts.add(new Part(id++, "flamethrower", new Weapon(4500, 10), PartGrade.REFINED));
        parts.add(new Part(id++, "golden_carp", new Weapon(2000, 15), PartGrade.REFINED));
        parts.add(new Part(id++, "trombone_cannon", new Weapon(4000, 15), PartGrade.REFINED));
        parts.add(new Part(id++, "eye_of_death", new Weapon(4500, 10), PartGrade.REFINED));

        parts.add(new Part(id++, "double_laser", new Weapon(2500, 5), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "sunflower", new Weapon(3500, 5), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "hearth", new Weapon(4495, 10), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "meteor_shower", new Weapon(3506, 10), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "golem_fist", new Weapon(3750, 10), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "root_whip", new Weapon(4500, 10), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "hidden_claw", new Weapon(5000, 15), PartGrade.SUPERIOR));

        parts.add(new Part(id++, "basketball_cannon", new Weapon(2800, 10), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "uncle_sam", new Weapon(3000, 10), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "skull", new Weapon(3000, 10), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "salmon_cannon", new Weapon(3000, 10), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "double_catcus", new Weapon(3500, 10), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "laser_TM", new Weapon(3500, 10), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "jaws", new Weapon(4000, 10), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "popcorn_launcher", new Weapon(4500, 10), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "horns_of_rage", new Weapon(4500, 10), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "bbq_gun", new Weapon(4500, 10), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "cat_mine", new Weapon(3750, 10), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "gyro_spirit", new Weapon(5500, 10), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "cat_drone", new Weapon(6000, 10000, 10), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "diamond_carp", new Weapon(4000, 15), PartGrade.OUTSTANDING));

        parts.add(new Part(id++, "mad_log", new Weapon(8000, 10), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "robot_head", new Weapon(8000, 10), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "spicy_barrage", new Weapon(9000, 15), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "hairball_thrower", new Weapon(9000, 15), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "water_trident", new Weapon(10000, 5), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "daruma_drone", new Weapon(8911, 15), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "kappa_drone", new Weapon(8800, 15), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "bento_drone", new Weapon(9901, 15), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "sonic_bell", new Weapon(8500, 10), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "blazing_dragon", new Weapon(9100, 10), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "bats", new Weapon(8000, 15), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "space_drill", new Weapon(8800, 10), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "plasma_sentry", new Weapon(4951, 5), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "acid_alien", new Weapon(9000, 10), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "sea_monster", new Weapon(10500, 15), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "gumball_gun", new Weapon(13650, 15), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "electric_eel", new Weapon(12000, 10), PartGrade.EXTRAORDINARY));

//--------------------------------------------------------------body---------------------------------------------------------------

        parts.add(new Part(id++, "dozer", new Chassis(11800, 20), PartGrade.STANDARD));
        parts.add(new Part(id++, "cutter", new Chassis(11800, 20), PartGrade.STANDARD));
        parts.add(new Part(id++, "cloud", new Chassis(16520, 30), PartGrade.STANDARD));

        parts.add(new Part(id++, "pig", new Chassis(18880, 15), PartGrade.POLISHED));
        parts.add(new Part(id++, "corsair", new Chassis(14160, 20), PartGrade.POLISHED));
        parts.add(new Part(id++, "fire_hazard", new Chassis(11800, 25), PartGrade.POLISHED));
        parts.add(new Part(id++, "land_bathyscaphe", new Chassis(15340, 25), PartGrade.POLISHED));
        parts.add(new Part(id++, "glacial_menace", new Chassis(14160, 30), PartGrade.POLISHED));

        parts.add(new Part(id++, "santas_sleigh", new Chassis(16520, 25), PartGrade.REFINED));
        parts.add(new Part(id++, "lantern", new Chassis(14160, 20), PartGrade.REFINED));
        parts.add(new Part(id++, "paws_rover", new Chassis(16520, 30), PartGrade.REFINED));

        parts.add(new Part(id++, "ufo", new Chassis(16520, 30), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "stove", new Chassis(25960, 30), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "apocatlypse_bus", new Chassis(16520, 25), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "coach", new Chassis(21240, 30), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "flower_power", new Chassis(25960, 30), PartGrade.SUPERIOR));

        parts.add(new Part(id++, "twin_mill", new Chassis(16520, 25), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "bone_shaker", new Chassis(16520, 25), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "shark_bite", new Chassis(16520, 25), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "alarm_5", new Chassis(17700, 25), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "tiger_shark", new Chassis(17700, 25), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "diamond_pig", new Chassis(14160, 30), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "metal_beast", new Chassis(18880, 30), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "golem", new Chassis(18880, 35), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "purr_mobile", new Chassis(18880, 35), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "stingy_bandit", new Chassis(21240, 35), PartGrade.OUTSTANDING));

        parts.add(new Part(id++, "tubby_bus", new Chassis(32568, 35), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "schrodintech", new Chassis(38409, 35), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "cool_ducky", new Chassis(49560, 35), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "iron_maiden", new Chassis(40120, 35), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "blossom_star", new Chassis(44181, 35), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "ram", new Chassis(35825, 35), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "phantom_circus", new Chassis(50697, 40), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "green_dragon", new Chassis(48392, 35), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "popsicle_beast", new Chassis(50858, 35), PartGrade.EXTRAORDINARY));

//--------------------------------------------------------------gadget---------------------------------------------------------------

        parts.add(new Part(id++, "cutter_repulse", new Gadget(4720, 5), PartGrade.STANDARD));
        parts.add(new Part(id++, "corsair_harpoon", new Gadget(4720, 5), PartGrade.STANDARD));
        parts.add(new Part(id++, "dozer_forklift", new Gadget(4720, 0), PartGrade.STANDARD));

        parts.add(new Part(id++, "inverse_thruster", new Gadget(4720, 0), PartGrade.POLISHED));
        parts.add(new Part(id++, "blasty_nudge", new Gadget(4720, 0), PartGrade.POLISHED));
        parts.add(new Part(id++, "lucky_horseshoe", new Gadget(4720, 5), PartGrade.POLISHED));

        parts.add(new Part(id++, "lotus", new Gadget(2360, -5), PartGrade.REFINED));
        parts.add(new Part(id++, "magic_lamp", new Gadget(2360, 5), PartGrade.REFINED));
        parts.add(new Part(id++, "santas_freezing_gift", new Gadget(4720, 5), PartGrade.REFINED));
        parts.add(new Part(id++, "nanobots_station", new Gadget(4720, 5, 28320), PartGrade.REFINED));
        parts.add(new Part(id++, "electrified_barbed_wire", new Gadget(4720, 5), PartGrade.REFINED));
        parts.add(new Part(id++, "energy_shield", new Gadget(2360, 10), PartGrade.REFINED));
        parts.add(new Part(id++, "frost_sprinkler", new Gadget(4720, 15), PartGrade.REFINED));

        parts.add(new Part(id++, "flue", new Gadget(4720, 5), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "electric_lasso", new Gadget(4720, 5), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "skull_goblet", new Gadget(5900, 5, 11800), PartGrade.SUPERIOR));

        parts.add(new Part(id++, "diamond_lotus", new Gadget(4720, -5), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "power_engine_tm", new Gadget(4720, 5), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "power_engine_bs", new Gadget(4720, 5), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "power_engine_sb", new Gadget(4720, 5), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "boxing_glove", new Gadget(4720, 5), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "firework", new Gadget(9440, 5), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "lifestone", new Gadget(9440, 10), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "stop_sign", new Gadget(4720, 10), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "scrambler", new Gadget(7080, 15), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "rune_of_protection", new Gadget(7080, 10), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "tiger_shark_engine", new Gadget(8260, 10), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "alarm_5_engine", new Gadget(8260, 10), PartGrade.OUTSTANDING));

        parts.add(new Part(id++, "paralyzing_potion", new Gadget(13334, 15), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "hungry_hook", new Gadget(14632, 10), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "geyser", new Gadget(18880, 10), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "water_gun", new Gadget(19470, 5), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "enrager", new Gadget(14632, 10), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "swapper", new Gadget(19116, 5), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "kitty_ghost", new Gadget(16284, 5), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "deflecting_shield", new Gadget(16520, 10), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "healing_drone", new Gadget(19540, 5), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "voodoo_doll", new Gadget(19116, 10), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "coffee_cup", new Gadget(20355, 0), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "squid_cannon", new Gadget(25090, 10), PartGrade.EXTRAORDINARY));

//--------------------------------------------------------------wheels---------------------------------------------------------------

        parts.add(new Part(id++, "dozer_guide_knob", new Wheel(4672), PartGrade.STANDARD));
        parts.add(new Part(id++, "santas_sticky_roller", new Wheel(4720), PartGrade.STANDARD));
        parts.add(new Part(id++, "corsair_guide_roller", new Wheel(4720), PartGrade.STANDARD));
        parts.add(new Part(id++, "fe_guide_roller", new Wheel(4720), PartGrade.STANDARD));
        parts.add(new Part(id++, "slow_roller", new Wheel(4720), PartGrade.STANDARD));
        parts.add(new Part(id++, "coach_roller", new Wheel(5192), PartGrade.STANDARD));
        parts.add(new Part(id++, "ict_guide_scooter", new Wheel(5192), PartGrade.STANDARD));
        parts.add(new Part(id++, "slow_scooter", new Wheel(5192), PartGrade.STANDARD));
        parts.add(new Part(id++, "cutter_tire", new Wheel(5970), PartGrade.STANDARD));
        parts.add(new Part(id++, "coach_tire", new Wheel(5970), PartGrade.STANDARD));
        parts.add(new Part(id++, "corsair_drive_tire", new Wheel(5970), PartGrade.STANDARD));
        parts.add(new Part(id++, "cutter_drive_bigfoot", new Wheel(7164), PartGrade.STANDARD));

        parts.add(new Part(id++, "ag_knob", new Wheel(4672), PartGrade.POLISHED));
        parts.add(new Part(id++, "dozer_drive_knob", new Wheel(4672), PartGrade.POLISHED));
        parts.add(new Part(id++, "ag_roller", new Wheel(5192), PartGrade.POLISHED));
        parts.add(new Part(id++, "ict_drive_scooter", new Wheel(5711), PartGrade.POLISHED));
        parts.add(new Part(id++, "ag_scooter_x_70", new Wheel(5711), PartGrade.POLISHED));
        parts.add(new Part(id++, "ag_scooter_x_19", new Wheel(5711), PartGrade.POLISHED));
        parts.add(new Part(id++, "fe_double_roller", new Wheel(5900), PartGrade.POLISHED));
        parts.add(new Part(id++, "santas_sticky_tire", new Wheel(6560), PartGrade.POLISHED));

        parts.add(new Part(id++, "stiff_roller", new Wheel(5192), PartGrade.REFINED));
        parts.add(new Part(id++, "dragon_knob", new Wheel(5310), PartGrade.REFINED));
        parts.add(new Part(id++, "dragon_roller", new Wheel(5900), PartGrade.REFINED));
        parts.add(new Part(id++, "tank_tracks", new Wheel(5900), PartGrade.REFINED));
        parts.add(new Part(id++, "train_guide_knob", new Wheel(5900), PartGrade.REFINED));

        parts.add(new Part(id++, "twin_mill_knob", new Wheel(6372), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "bone_shaker_knob", new Wheel(6372), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "twin_mill_roller", new Wheel(7080), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "bone_shaker_roller", new Wheel(7080), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "shark_bite_roller", new Wheel(7080), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "big_tank_tracks", new Wheel(7080), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "train_double_roller", new Wheel(7080), PartGrade.SUPERIOR));
        parts.add(new Part(id++, "shark_bite_scooter", new Wheel(7788), PartGrade.SUPERIOR));

        parts.add(new Part(id++, "diamond_slow_roller", new Wheel(5900), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "diamond_slow_scooter", new Wheel(6490), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "golem_tracks", new Wheel(7080), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "stingy_knob", new Wheel(7080), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "bandit_knob", new Wheel(7080), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "rogue_knob", new Wheel(7080), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "boost_knob", new Wheel(7080), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "boost_scooter", new Wheel(9440), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "alarm_5_tire", new Wheel(8260, 1000), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "alarm_5_bigfoot", new Wheel(8968, 1000), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "tiger_shark_tire", new Wheel(8260, 1500), PartGrade.OUTSTANDING));
        parts.add(new Part(id++, "tiger_shark_bigfoot", new Wheel(8968, 1500), PartGrade.OUTSTANDING));

        parts.add(new Part(id++, "death_scooter", new Wheel(9440), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "life_scooter", new Wheel(9440), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "nigiri_knob", new Wheel(8260), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "naruto_knob", new Wheel(8260), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "maki_knob", new Wheel(8260), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "sand_scooter", new Wheel(17700), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "sand_tire", new Wheel(20060), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "shuttle_knob", new Wheel(9086), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "space_knob", new Wheel(9086), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "flower_hover", new Wheel(10384, 1320), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "onion_thruster", new Wheel(10384, 1320), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "big_pumpkin_wheel", new Wheel(8260, 1750), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "small_pumpkin_wheel", new Wheel(7670, 1250), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "seashell_roller", new Wheel(18880), PartGrade.EXTRAORDINARY));
        parts.add(new Part(id++, "coconut_knob", new Wheel(9440), PartGrade.EXTRAORDINARY));

//--------------------------------------------------------------wheels---------------------------------------------------------------
    }

    public static List<Part> getPartsByTypeAndGrade(PartType partType, PartGrade partGrade) {
        return parts.stream()
                .filter(part -> part.type().getPartType().equals(partType) && part.grade().equals(partGrade))
                .toList();
    }

    public static Part getPartById(int partId) {
        return parts.stream().filter(p -> p.id() == partId).findAny().orElse(null);
    }

    @SneakyThrows
    public static String callbackDataJson(Map<String, String> callbackDataMap) {
        return objectMapper.writeValueAsString(callbackDataMap);
    }

    public static String getOptionalStat(boolean hasStat, String label, int level, IntSupplier statGetter) {
        return hasStat ? formatSingleStat(label, level, statGetter) : EMPTY_STRING;
    }

    public static String formatDoubleStat(String label1, int level, IntSupplier statGetter, String label2, int value2) {
        return String.format(DOUBLE_STAT_PATTERN, label1, getStatValue(level, statGetter), label2, value2);
    }

    public static String formatSingleStat(String label, int level, IntSupplier statGetter) {
        return String.format(SINGLE_STAT_PATTERN, label, getStatValue(level, statGetter));
    }

    public static String getStatValue(int level, IntSupplier statGetter) {
        return level > 0 ? String.valueOf(statGetter.getAsInt()) : QUESTION;
    }

    public static String formatStatDiff(String label, int newLevel, int baseLevel, IntFunction<Integer> statGetter) {
        return formatOptionalStatDiff(true, label, newLevel, baseLevel, statGetter);
    }

    public static String formatOptionalStatDiff(boolean hasStat, String label,
                                                int newLevel, int baseLevel,
                                                IntFunction<Integer> statGetter) {
        return hasStat ? formatStatDiff(label, statGetter.apply(newLevel), calcDiff(newLevel, baseLevel, statGetter))
                : EMPTY_STRING;
    }

    private static int calcDiff(int newLevel, int baseLevel, IntFunction<Integer> statGetter) {
        return statGetter.apply(newLevel) - statGetter.apply(baseLevel);
    }

    private static String formatStatDiff(String label, int newValue, int diff) {
        return String.format(STAT_DIFF_PATTERN, label, newValue, diff);
    }

    public static List<PossibleUpgrade> calculateStatsAfterUpgrade(Part part, int level, int spare) {
        int[] accumulatedCurrency = {0};
        int[] accumulatedTokens = {0};
        int[] remainingParts = {spare};

        return IntStream.range(level - 1, part.grade().getUpgrades().size())
                .takeWhile(i -> remainingParts[0] >= part.grade().getUpgrades().get(i).parts())
                .mapToObj(i -> {
                    Upgrade upgrade = part.grade().getUpgrades().get(i);
                    accumulatedCurrency[0] += upgrade.currency();
                    accumulatedTokens[0] += upgrade.tokens();
                    remainingParts[0] -= upgrade.parts();

                    return new PossibleUpgrade(
                            accumulatedCurrency[0],
                            accumulatedTokens[0],
                            remainingParts[0],
                            i + 1,
                            part.type().diff(i + 2, level)
                    );
                })
                .collect(Collectors.toList());
    }

    public static InlineKeyboardMarkup inlineKeyboardMarkup(List<InlineKeyboardRow> rowsInline) {
        return InlineKeyboardMarkup.builder().keyboard(rowsInline).build();
    }

    public static InlineKeyboardMarkup inlineKeyboardMarkup(InlineKeyboardRow... rows) {
        return InlineKeyboardMarkup.builder().keyboard(List.of(rows)).build();
    }

    public static InlineKeyboardButton inlineKeyboardButton(String buttonText, Map<String, String> callbackData) {
        return InlineKeyboardButton.builder().text(buttonText).callbackData(callbackDataJson(callbackData)).build();
    }

    public record PossibleUpgrade(int requiredCurrency, int requiredTokens, int remainingParts, int levelAfterUpgrade,
                                  String statDiff) {
    }
}
