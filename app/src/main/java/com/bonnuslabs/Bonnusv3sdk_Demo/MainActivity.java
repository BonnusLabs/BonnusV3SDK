package com.bonnuslabs.Bonnusv3sdk_Demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import mx.bonnusSdk.bonnuslabs.Bonnus;
import mx.bonnuslabs.Bonnusv3sdk_Demo.R;

public class MainActivity extends AppCompatActivity {

    private static final String ACCESS_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ik56QkJRalF4TWpaR01EZ3dRemt6T1RNMk5rUTRRMFJHUmtaQk5FTkVOMFU0UkVRMk1UVkZPQSJ9.eyJpc3MiOiJodHRwczovL2Jvbm51cy5hdXRoMC5jb20vIiwic3ViIjoiRW8yVEdrc3VWcFg4MndXT0Iwc24zR1pFemhadU5mdW1AY2xpZW50cyIsImF1ZCI6Imh0dHBzOi8vYm9ubnVzYXBpMzAuYXp1cmV3ZWJzaXRlcy5uZXQvIiwiaWF0IjoxNTY4OTI3NTYyLCJleHAiOjE1NzE1MTk1NjIsImF6cCI6IkVvMlRHa3N1VnBYODJ3V09CMHNuM0daRXpoWnVOZnVtIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIn0.pcT0na0mZxrihn3Gn8G6uPCYIFHc8KVADr9cWAFoNyQ91QEPPAmXG90gj0FsqK8CWZqZ-5pvZDGSjT_PTCS6ZdTn7J-C0cvreG390qpFodrDRcP1gTDuNuZmU6Jyd8FVHZmnb76sypHQG-pgIIrR2g8GlFiubjmbNsHhV0Fb2446CeTe3RqabMlteydozV6r4oMIpV-iplOgdSPqARGL7iNa1-tmK3MubzVquHjMBVrbe22IXmptey632By-QEXVTaB-FHCGfLxDEfPoZa3-AILeMoCJmcTWRDFCqOymkcq8IrwNwGELrbrz4XRHamj23pIYIdL-UQkdMcLb155lXQ";
    public static final String URL_TO_LOAD = "urlToLoad";

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);

        getSdkUserId();

        Button initButton = findViewById(R.id.initialize_button);
        initButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bonnusInitialize();
            }
        });

        Button momentButton = findViewById(R.id.trigger_button);
        momentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bonnusMoment();
            }
        });

        Button listButton = findViewById(R.id.list_button);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bonnusRewards();
            }
        });
    }

    private void getSdkUserId(){
        String value = Bonnus.getInstance().getUserId(MainActivity.this);
        if(value != null && !value.isEmpty()){
            Toast.makeText(MainActivity.this, value, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "No Sdk User", Toast.LENGTH_SHORT).show();
        }
    }

    private void bonnusInitialize(){
        progressBar.setVisibility(View.VISIBLE);

        Dictionary<String, String> dictionary = new Hashtable<>();
        dictionary.put("name","Tomas");
        dictionary.put("lastName","Gonzalez");

        Bonnus.getInstance().requestInitialize(
                "000006",
                dictionary,
                ACCESS_TOKEN,
                MainActivity.this,
                true,
                new Bonnus.OnBonnusSdkInitResponse() {
                    @Override
                    public void initializeResponse(Object value, String responseMessage) {
                        progressBar.setVisibility(View.GONE);
                        if(value != null){
                            Toast.makeText(MainActivity.this, value.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void bonnusMoment(){
        progressBar.setVisibility(View.VISIBLE);

        Bonnus.getInstance().sendMoment(
                "prueba0",
                MainActivity.this,
                new Bonnus.OnBonnusSdkMomentResponse() {
                    @Override
                    public void momentResponse(Object value, String responseMessage) {
                        progressBar.setVisibility(View.GONE);
                        if(value != null && !value.toString().isEmpty()){
                            Intent intent = new Intent(MainActivity.this, BonnusWebAppActivity.class);
                            intent.putExtra(URL_TO_LOAD,value.toString());
                            startActivity(intent);
                        }
                    }
                }
        );
    }

    private void bonnusRewards(){
        progressBar.setVisibility(View.VISIBLE);

        Bonnus.getInstance().requestRewards(false, MainActivity.this, new Bonnus.OnBonnusSdkListResponse() {
            @Override
            public void listResponse(Object value, String responseMessage) {
                progressBar.setVisibility(View.GONE);
                if(value instanceof String){
                    Intent intent = new Intent(MainActivity.this, BonnusWebAppActivity.class);
                    intent.putExtra(URL_TO_LOAD,value.toString());
                    startActivity(intent);
                } else if(value instanceof ArrayList){
                    Toast.makeText(MainActivity.this, "List of Rewards as List", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bonnus.getInstance().terminateSdk();
    }
}
