package com.example.firstrestapi;

import com.example.firstrestapi.dto.ProductDetail;
import com.example.firstrestapi.dto.ProductLanguageTranslation;

import java.util.List;

public interface TestData {

    interface Category {
        String LAPTOP = "laptop";
    }

    interface ProductLanguageTranslations {
        ProductLanguageTranslation SAMPLE_TRANSLATION_DE = new ProductLanguageTranslation(
                "de",
                "MacBook Pro",
                List.of(
                        ProductDetails.SIZE_DE,
                        ProductDetails.BATTERY_DE,
                        ProductDetails.MATERIAL_DE,
                        ProductDetails.SCREEN_DE
                )
        );

        ProductLanguageTranslation SAMPLE_TRANSLATION_EN = new ProductLanguageTranslation(
                "en",
                "MacBook Pro English",
                List.of(
                        ProductDetails.SIZE_EN,
                        ProductDetails.BATTERY_EN,
                        ProductDetails.MATERIAL_EN,
                        ProductDetails.SCREEN_EN
                )
        );
    }

    interface ProductDetails {
        // Size details
        ProductDetail SIZE_DE = new ProductDetail("Groesse", "12 Zoll");
        ProductDetail SIZE_EN = new ProductDetail("Size", "12 Inch");

        // Battery details
        ProductDetail BATTERY_DE = new ProductDetail("Batterie", "3000 mAh");
        ProductDetail BATTERY_EN = new ProductDetail("Battery", "3000 mAh");

        // Color details
        ProductDetail COLOR_DE = new ProductDetail("Farbe", "Schwarz");
        ProductDetail COLOR_EN = new ProductDetail("Color", "Black");

        // Material details
        ProductDetail MATERIAL_DE = new ProductDetail("Material", "Aluminium");
        ProductDetail MATERIAL_EN = new ProductDetail("Material", "Aluminum");

        // Weight details
        ProductDetail WEIGHT_DE = new ProductDetail("Gewicht", "1.5 kg");
        ProductDetail WEIGHT_EN = new ProductDetail("Weight", "1.5 kg");

        // Warranty details
        ProductDetail WARRANTY_DE = new ProductDetail("Garantie", "2 Jahre");
        ProductDetail WARRANTY_EN = new ProductDetail("Warranty", "2 years");

        // Screen details
        ProductDetail SCREEN_DE = new ProductDetail("Bildschirm", "15.6 Zoll Full HD");
        ProductDetail SCREEN_EN = new ProductDetail("Screen", "15.6 Inch Full HD");

        // Processor details
        ProductDetail PROCESSOR_DE = new ProductDetail("Prozessor", "Intel i7");
        ProductDetail PROCESSOR_EN = new ProductDetail("Processor", "Intel i7");

        // Storage details
        ProductDetail STORAGE_DE = new ProductDetail("Speicher", "512 GB SSD");
        ProductDetail STORAGE_EN = new ProductDetail("Storage", "512 GB SSD");

        // Connectivity details
        ProductDetail CONNECTIVITY_DE = new ProductDetail("Konnektivität", "Wi-Fi 6, Bluetooth 5.0");
        ProductDetail CONNECTIVITY_EN = new ProductDetail("Connectivity", "Wi-Fi 6, Bluetooth 5.0");

        // Camera details
        ProductDetail CAMERA_DE = new ProductDetail("Kamera", "12 MP");
        ProductDetail CAMERA_EN = new ProductDetail("Camera", "12 MP");

        // Operating System details
        ProductDetail OS_DE = new ProductDetail("Betriebssystem", "Windows 11");
        ProductDetail OS_EN = new ProductDetail("Operating System", "Windows 11");

        // Charging details
        ProductDetail CHARGING_DE = new ProductDetail("Laden", "USB-C Schnellladung");
        ProductDetail CHARGING_EN = new ProductDetail("Charging", "USB-C Fast Charging");

        // Screen Resolution details
        ProductDetail RESOLUTION_DE = new ProductDetail("Auflösung", "1920x1080");
        ProductDetail RESOLUTION_EN = new ProductDetail("Resolution", "1920x1080");



    }

}
