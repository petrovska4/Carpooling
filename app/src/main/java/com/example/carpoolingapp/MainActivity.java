package com.example.carpoolingapp;

import static com.example.carpoolingapp.FirebaseUtils.fetchUserDriverStatus;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.Menu;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private ImageButton menuIcon;
    private boolean driver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fetchUserDriverStatus(new FirebaseUtils.FirebaseUserCallback() {
            @Override
            public void onSuccess(Boolean driverStatus) {
                driver = driverStatus;
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        menuIcon = findViewById(R.id.menu_icon);

        menuIcon.setOnClickListener(v -> showMenu());


    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuIcon);

        Menu menu = popupMenu.getMenu();
        if (!driver) {
            menu.add(Menu.NONE, 5, 5, "My trips");
        } else {
            menu.add(Menu.NONE, 2, 2, "Post Vehicle");
            menu.add(Menu.NONE, 6, 6, "My Vehicles");
        }

        menu.add(Menu.NONE, 3, 3, "View all vehicles");
        menu.add(Menu.NONE, 4, 4, "View all trips");
        menu.add(Menu.NONE, 7, 7, "Logout");

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case 1:
                    Toast.makeText(MainActivity.this, "My Trips selected", Toast.LENGTH_SHORT).show();
                    return true;
                case 2:
                    Toast.makeText(MainActivity.this, "Post Vehicle selected", Toast.LENGTH_SHORT).show();
                    Intent postTripIntent = new Intent(MainActivity.this, PostVehicleActivity.class);
                    startActivity(postTripIntent);
                    return true;
                case 3:
                    Toast.makeText(MainActivity.this, "View all Vehicles selected", Toast.LENGTH_SHORT).show();
                    Intent viewAllVehiclesIntent = new Intent(MainActivity.this, ViewAllVehiclesActivity.class);
                    startActivity(viewAllVehiclesIntent);
                    return true;
                case 4:
                    Toast.makeText(MainActivity.this, "View all Trips selected", Toast.LENGTH_SHORT).show();
                    Intent viewAllTripsIntent = new Intent(MainActivity.this, ViewAllTripsActivity.class);
                    startActivity(viewAllTripsIntent);
                    return true;
                case 5:
                    Toast.makeText(MainActivity.this, "My Trips selected", Toast.LENGTH_SHORT).show();
                    Intent myTripsIntent = new Intent(MainActivity.this, MyTripsActivity.class);
                    startActivity(myTripsIntent);
                    return true;
                case 6:
                    Toast.makeText(MainActivity.this, "My Vehicles selected", Toast.LENGTH_SHORT).show();
                    Intent myVehiclesIntent = new Intent(MainActivity.this, MyVehiclesActivity.class);
                    startActivity(myVehiclesIntent);
                    return true;
                case 7:
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                default:
                    return false;
            }
        });
    }


}