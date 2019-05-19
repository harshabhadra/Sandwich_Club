package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    //Declaring variables for Views we're going to populate
    private TextView origin_label;
    private TextView origin;
    private TextView alsoKnown_label;
    private TextView alsoKnown;
    private TextView ingredients;
    private TextView ingredientsLabel;
    private TextView descriptionLabel;
    private TextView description;
    private ImageView ingredientsIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Finding Views by id's to populate these views
        ingredientsIv = findViewById(R.id.image_iv);
        origin = findViewById(R.id.origin_tv);
        origin_label = findViewById(R.id.place_of_origin_label);
        alsoKnown_label = findViewById(R.id.also_known_label);
        alsoKnown = findViewById(R.id.also_known_tv);
        ingredientsLabel = findViewById(R.id.detail_ingredients_label);
        ingredients = findViewById(R.id.ingredients_tv);
        descriptionLabel = findViewById(R.id.description_tv_label);
        description = findViewById(R.id.description_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }
        populateUI(sandwich);

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        //Populating the ImageView  with Picasso library
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        //Setting the Title of the Detail Layout
        setTitle(sandwich.getMainName());

        if (sandwich.getPlaceOfOrigin().isEmpty()){

            //If place_of_origin is not parsed then hide these two views
            origin_label.setVisibility(View.GONE);
            origin.setVisibility(View.GONE);
        }else {

            //Populate the origin TextView
            origin.setText(sandwich.getPlaceOfOrigin());
        }

        if (sandwich.getAlsoKnownAs().isEmpty()){

            //If also_known_as is not parsed then hide these two views
            alsoKnown_label.setVisibility(View.GONE);
            alsoKnown.setVisibility(View.GONE);
        }else {

            //Populate also_known_as TextView
            List<String> alsoString = sandwich.getAlsoKnownAs();
            String akaString = TextUtils.join(", ", alsoString);
            alsoKnown.append(akaString);
        }

        if (sandwich.getDescription().isEmpty()){

            //If description is not parsed then hide these two views
            descriptionLabel.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
        }else {

            //Populate the description TextView
            description.setText(sandwich.getDescription());
        }

        if (sandwich.getIngredients().isEmpty()){

            //If ingredients are not parsed then hide these two views
            ingredientsLabel.setVisibility(View.GONE);
            ingredients.setVisibility(View.GONE);
        }else {

            //Populate ingredients TextView
            List<String> stringList = sandwich.getIngredients();
            String ingredientsString = TextUtils.join(", ", stringList);
            ingredients.setText(ingredientsString);
        }

    }
}
