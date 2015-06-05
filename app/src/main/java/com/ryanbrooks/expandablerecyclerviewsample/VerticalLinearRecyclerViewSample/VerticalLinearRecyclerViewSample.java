package com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.ryanbrooks.expandablerecyclerviewsample.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnItemSelected;

/**
 * Created by Ryan Brooks on 5/29/15.
 */
public class VerticalLinearRecyclerViewSample extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    private MyExpandableAdapter mExpandableAdapter;
    private ArrayList<Long> mDurationList;

    @InjectView(R.id.vertical_sample_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.vertical_recyclerview_sample)
    RecyclerView mRecyclerView;
    @InjectView(R.id.vertical_sample_toolbar_checkbox)
    CheckBox mAnimationEnabledCheckBox;
    @InjectView(R.id.vertical_sample_toolbar_spinner)
    Spinner mToolbarSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_recyclerview_sample);
        ButterKnife.inject(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDurationList = generateSpinnerSpeeds();

        mExpandableAdapter = new MyExpandableAdapter(this, setUpTestData(20));
        mRecyclerView.setAdapter(mExpandableAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(this, mDurationList);
        mToolbarSpinner.setAdapter(customSpinnerAdapter);
        // Initial setting
        Log.d(TAG, "Initial item selected: " + mToolbarSpinner.getSelectedItemPosition());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState = mExpandableAdapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mExpandableAdapter.onRestoreInstanceState(savedInstanceState);
    }

    @OnItemSelected(R.id.vertical_sample_toolbar_spinner)
    void onItemSelected(int position) {
        if (mAnimationEnabledCheckBox.isChecked()) {
            if (mDurationList.get(position) == 0) {
                mExpandableAdapter.setParentClickableViewAnimationDuration(
                        mExpandableAdapter.CUSTOM_ANIMATION_DURATION_NOT_SET);
            } else {
                mExpandableAdapter.setParentClickableViewAnimationDuration(mDurationList.get(position));
            }
            mExpandableAdapter.setParentAndIconExpandOnClick(false);
        } else {
            if (mDurationList.get(position) == 0) {
                mExpandableAdapter.setParentClickableViewAnimationDuration(
                        mExpandableAdapter.CUSTOM_ANIMATION_DURATION_NOT_SET);
                mExpandableAdapter.setParentAndIconExpandOnClick(false);
            } else {
                mExpandableAdapter.setParentClickableViewAnimationDuration(mDurationList.get(position));
                mExpandableAdapter.setCustomParentAnimationViewId(R.id.recycler_item_arrow_parent);
                mExpandableAdapter.setParentAndIconExpandOnClick(true);
            }
        }
        mExpandableAdapter.notifyDataSetChanged();
    }

    @OnCheckedChanged(R.id.vertical_sample_toolbar_checkbox)
    void onCheckChanged(boolean isChecked) {
        if (isChecked) {
            mExpandableAdapter.setParentAndIconExpandOnClick(false);
            mExpandableAdapter.notifyDataSetChanged();
        } else {
            mExpandableAdapter.setParentAndIconExpandOnClick(true);
            mExpandableAdapter.setCustomParentAnimationViewId(R.id.recycler_item_arrow_parent);
            mExpandableAdapter.setParentClickableViewAnimationDuration((Long) mToolbarSpinner.getSelectedItem());
            mExpandableAdapter.notifyDataSetChanged();
        }
    }

    private ArrayList<Object> setUpTestData(int numItems) {
        ArrayList<Object> data = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            CustomChildObject customChildObject = new CustomChildObject();
            customChildObject.setChildText("Child " + i);

            CustomParentObject customParentObject = new CustomParentObject();

            customChildObject.setParentObject(customParentObject); //TODO: How can this be done better
            customParentObject.setChildObject(customChildObject);
            customParentObject.setParentNumber(i);
            customParentObject.setParentText("Parent " + i);
            data.add(customParentObject);
        }
        return data;
    }

    private ArrayList<Long> generateSpinnerSpeeds() {
        long initialSpeed = 100;
        ArrayList<Long> speedList = new ArrayList<>();
        speedList.add(mExpandableAdapter.CUSTOM_ANIMATION_DURATION_NOT_SET);
        for (int i = 1; i <= 10; i++) {
            speedList.add(initialSpeed * i);
        }
        return speedList;
    }
}
