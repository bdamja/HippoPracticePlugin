package practice.hippo.util;

public class BiomeType {
    public static int idOf(String name) {
        int id = -1;
        switch (name) {
            case "null":
                id = -1;
                break;
            case "ocean":
                id = 0;
                break;
            case "plains":
                id = 1;
                break;
            case "desert":
                id = 2;
                break;
            case "extreme_hills":
                id = 3;
                break;
            case "forest":
                id = 4;
                break;
            case "taiga":
                id = 5;
                break;
            case "swampland":
                id = 6;
                break;
            case "river":
                id = 7;
                break;
            case "hell":
                id = 8;
                break;
            case "the_end":
                id = 9;
                break;
            case "frozen_ocean":
                id = 10;
                break;
            case "frozen_river":
                id = 11;
                break;
            case "ice_plains":
                id = 12;
                break;
            case "ice_mountains":
                id = 13;
                break;
            case "mushroom_island":
                id = 14;
                break;
            case "mushroom_island_shore":
                id = 15;
                break;
            case "beach":
                id = 16;
                break;
            case "desert_hills":
                id = 17;
                break;
            case "forest_hills":
                id = 18;
                break;
            case "taiga_hills":
                id = 19;
                break;
            case "extreme_hills_edge":
                id = 20;
                break;
            case "jungle":
                id = 21;
                break;
            case "jungle_hills":
                id = 22;
                break;
            case "jungle_edge":
                id = 23;
                break;
            case "deep_ocean":
                id = 24;
                break;
            case "stone_beach":
                id = 25;
                break;
            case "cold_beach":
                id = 26;
                break;
            case "birch_forest":
                id = 27;
                break;
            case "birch_forest_hills":
                id = 28;
                break;
            case "roofed_forest":
                id = 29;
                break;
            case "cold_taiga":
                id = 30;
                break;
            case "cold_taiga_hills":
                id = 31;
                break;
            case "mega_taiga":
                id = 32;
                break;
            case "mega_taiga_hills":
                id = 33;
                break;
            case "extreme_hills+":
                id = 34;
                break;
            case "savanna":
                id = 35;
                break;
            case "savanna_plateau":
                id = 36;
                break;
            case "mesa":
                id = 37;
                break;
            case "mesa_plateau_f":
                id = 38;
                break;
            case "mesa_plateau":
                id = 39;
                break;
            case "the_void":
                id = 127;
                break;
            case "sunflower_plains":
                id = 129;
                break;
            case "desert_m":
                id = 130;
                break;
            case "extreme_hills_m":
                id = 131;
                break;
            case "flower_forest":
                id = 132;
                break;
            case "taiga_m":
                id = 133;
                break;
            case "swampland_m":
                id = 134;
                break;
            case "ice_plains_spikes":
                id = 140;
                break;
            case "jungle_m":
                id = 149;
                break;
            case "jungle_edge_m":
                id = 151;
                break;
            case "birch_forest_m":
                id = 155;
                break;
            case "birch_forest_hills_m":
                id = 156;
                break;
            case "roofed_forest_m":
                id = 157;
                break;
            case "cold_taiga_m":
                id = 158;
                break;
            case "mega_spruce_taiga":
                id = 160;
                break;
            case "redwood_taiga_hills_m":
                id = 161;
                break;
            case "extreme_hills+_m":
                id = 162;
                break;
            case "savanna_m":
                id = 163;
                break;
            case "savanna_plateau_m":
                id = 164;
                break;
            case "mesa_m":
                id = 165;
                break;
            case "mesa_plateau_f_m":
                id = 166;
                break;
            case "mesa_plateau_m":
                id = 167;
                break;
        }
        return id;
    }
}
