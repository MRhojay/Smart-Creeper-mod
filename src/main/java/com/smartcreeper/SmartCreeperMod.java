package com.smartcreeper;
‎
‎import net.fabricmc.api.ModInitializer;
‎
‎public class SmartCreeperMod implements ModInitializer {
‎
‎public static final String MOD_ID = "smartcreeper";  
‎
‎@Override  
‎public void onInitialize() {  
‎    System.out.println("Smart Creeper mod loaded successfully!");  
‎    // All creeper behavior modifications are handled via mixins.  
‎}
}
