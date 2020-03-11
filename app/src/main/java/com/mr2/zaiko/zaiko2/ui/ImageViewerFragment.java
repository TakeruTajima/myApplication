package com.mr2.zaiko.zaiko2.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.mr2.zaiko.R;


public class ImageViewerFragment extends Fragment {
    /* ---------------------------------------------------------------------- */
    /* Field                                                                  */
    /* ---------------------------------------------------------------------- */
    public static final String TAG = ImageViewerFragment.class.getSimpleName() + "(4156)";
    public static final String KEY_IMAGE_PATH = "imagePath";
    public static final String KEY_CAN_BE_ADDED = "canBeAdded";
    public static final String KEY_COMMODITY_ID = "commodityId";
    private View view = null;
    private Context context;
    private ViewPager2 viewPager2Vertical;
    private ViewPager2 viewPager2Horizontal;

    /*リスナーを使う時はこのコメントを外す*/
//    private ImageViewerFragmentListener listener = null;

    /* ---------------------------------------------------------------------- */
    /* Listener                                                               */
    /* ---------------------------------------------------------------------- */
    /*リスナーを使う時はこのコメントを外す*/
//    public interface ImageViewerFragmentListener {
//        void onHogeEvent();
//    }

    /* ---------------------------------------------------------------------- */
    /* Lifecycle                                                              */
    /* ---------------------------------------------------------------------- */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
        this.context = context;

        /*リスナーを使う時はこのコメントを外す*/
//        if (!(context instanceof ItemDataActivityFragmentListener)) {
//            throw new UnsupportedOperationException(
//                    TAG + ":" + "Listener is not Implementation.");
//        } else {
//            listener = (ItemDataActivityFragmentListener) context;
//        }
//        this.activity = (Activity) context;



    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_image_viewer, container, false);
//        viewPager2Vertical = view.findViewById(R.id.image_viewer_view_pager2_vertical);
//        ImageViewerFragmentStateAdapter adapter = new ImageViewerFragmentStateAdapter(this, new ImageViewerResource());
//        viewPager2Vertical.setAdapter(adapter);
//        viewPager2Vertical.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        //スクロールしたときにFragmentを終わらせるリスナーを登録してええええ
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }

    /* ---------------------------------------------------------------------- */
    /* other method                                                           */
    /* ---------------------------------------------------------------------- */

}

