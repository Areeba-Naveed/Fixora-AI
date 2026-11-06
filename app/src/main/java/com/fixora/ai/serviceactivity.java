package com.fixora.ai;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class serviceactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serviceactivity);

        String service = getIntent().getStringExtra("service");
        if (service == null) service = "Electrician";

        ((TextView) findViewById(R.id.tvCategoryTitle)).setText(service);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        String[] names;
        String[] icons;

        if (service.equals("AC Technician")) {
            names = new String[]{"AC Repair", "AC Installation", "Gas Refilling",
                    "AC Servicing & Cleaning", "Other Services"};
            icons = new String[]{"🔧", "❄", "💨", "🧹", "•••"};
        } else if (service.equals("Plumber")) {
            names = new String[]{"Pipe Leakage Repair", "Tap & Mixer Fitting",
                    "Drain Blockage", "Water Tank & Motor", "Other Services"};
            icons = new String[]{"🔧", "🚰", "🪠", "💧", "•••"};
        } else if (service.equals("Appliance Repair")) {
            names = new String[]{"Refrigerator Repair", "Washing Machine Repair",
                    "Microwave & Oven Repair", "Water Dispenser / Geyser", "Other Appliances"};
            icons = new String[]{"🧊", "🧺", "🍳", "🚰", "•••"};
        } else if (service.equals("Carpenter")) {
            names = new String[]{"Furniture Repair", "Door & Window Fitting",
                    "Cupboard & Wardrobe", "Custom Woodwork", "Other Services"};
            icons = new String[]{"🪑", "🚪", "🗄", "🪚", "•••"};
        } else if (service.equals("Painter")) {
            names = new String[]{"Interior Painting", "Exterior Painting",
                    "Wall Putty & Polish", "Waterproofing & Damp", "Other Services"};
            icons = new String[]{"🎨", "🏠", "🧱", "💧", "•••"};
        } else {
            names = new String[]{"Electrical Repair", "Wiring & Installation",
                    "Switch & Socket", "Lighting Solutions", "Other Services"};
            icons = new String[]{"⚡", "🔌", "🔘", "💡", "•••"};
        }

        int[] rowIds = {R.id.row1, R.id.row2, R.id.row3, R.id.row4, R.id.row5};
        int[] nameIds = {R.id.nm1, R.id.nm2, R.id.nm3, R.id.nm4, R.id.nm5};
        int[] iconIds = {R.id.ic1, R.id.ic2, R.id.ic3, R.id.ic4, R.id.ic5};

        for (int i = 0; i < 5; i++) {
            final String name = names[i];
            ((TextView) findViewById(nameIds[i])).setText(name);
            ((TextView) findViewById(iconIds[i])).setText(icons[i]);
            findViewById(rowIds[i]).setOnClickListener(v ->
                    Toast.makeText(this, name + " selected", Toast.LENGTH_SHORT).show());
        }

        findViewById(R.id.bannerHelp).setOnClickListener(v ->
                Toast.makeText(this, "Support chat coming soon", Toast.LENGTH_SHORT).show());
    }
}