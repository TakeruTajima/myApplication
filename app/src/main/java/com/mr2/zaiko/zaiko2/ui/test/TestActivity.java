package com.mr2.zaiko.zaiko2.ui.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.OperationCanceledException;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import com.mr2.zaiko.R;
import com.mr2.zaiko.zaiko2.TestApplication;
import com.mr2.zaiko.zaiko2.domain.inhouse.equipment.Photo;
import com.mr2.zaiko.zaiko2.loader.TestLoaderProgressEvent;
import com.mr2.zaiko.zaiko2.loader.TestTaskLoader;
import com.mr2.zaiko.zaiko2.ui.ImageCapture.ImageCaptureActivity;
import com.mr2.zaiko.zaiko2.ui.ImageViewer.ImageViewerFragment;
import com.mr2.zaiko.zaiko2.ui.ImageViewer.ImageViewerResource;
import com.mr2.zaiko.zaiko2.ui.adapter.EventBusService;
import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.PictureResult;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity implements ContractTest.View, LoaderManager.LoaderCallbacks<String> {
    public static final String TAG = TestActivity.class.getSimpleName() + "(4156)";
    /* ---------------------------------------------------------------------- */
    /* Field                                                                  */
    /* ---------------------------------------------------------------------- */

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss.SSS");
    private ContractTest.Presenter presenter;
    private final int LOADER_ID = 0;
    private String loaderResult;
    private final String KEY_LOADER_RESULT = "loader_result";

    /* ---------------------------------------------------------------------- */
    /* Listener                                                               */
    /* ---------------------------------------------------------------------- */


    /* ---------------------------------------------------------------------- */
    /* Lifecycle                                                              */
    /* ---------------------------------------------------------------------- */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        System.out.println("/////////////// TestActivity onCreate() ***");

        setViews();
        setPresenter();
        presenter.onCreate(this);

        if (null != savedInstanceState) loaderResult = savedInstanceState.getString(KEY_LOADER_RESULT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        checkLoader();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        registerSubscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        unregisterSubscribe();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putString(KEY_LOADER_RESULT, loaderResult);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        presenter.onDestroy(this);
    }

    private void setViews(){
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(view -> presenter.event_1());
        findViewById(R.id.button2).setOnClickListener(view -> startNewActivity());
        findViewById(R.id.button3).setOnClickListener(view -> startImageViewerFragment());
        findViewById(R.id.button4).setOnClickListener(view -> startLoader());
        ((ProgressBar)findViewById(R.id.progressBar)).setProgress(0);
    }

    public void startImageViewerFragment(){
        ImageViewerFragment fragment = new ImageViewerFragment();
        fragment.setArguments(getResource().toArguments());

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_frame_layout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    private ImageViewerResource getResource(){
        List<Photo> photos = new ArrayList<>();
        for (int i = 0; 3 > i; i++)
            photos.add(new Photo("20200309033935.jpg"));
        return new ImageViewerResource(photos, getFilesDir().getAbsolutePath());
    }

    private void startNewActivity(){
        Intent intent = new Intent(getApplication(), ImageCaptureActivity.class);
        startActivity(intent);
    }

    private void setPresenter(){
        TestApplication application = (TestApplication) getApplication();
        if (null != application) {
            presenter = application.testPresenter();
        }else {
            System.out.println("///////////////////////Applicationを取得できませんでした。");
            throw new IllegalStateException("missing application.");
        }
    }

    public void createFile(byte[] data){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");
        String s = ZonedDateTime.now().format(dateTimeFormatter) + ".jpg";
        try {
            FileOutputStream fos = openFileOutput(s, MODE_PRIVATE);
//            fos.write();
        }catch (FileNotFoundException e){
            e.printStackTrace();
            showToast(e.getMessage());
        }
    }

    public byte[] compressJPG(PictureResult result, String fileName){
        try {
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            BitmapCallback bmc = new BitmapCallback() {
                @Override
                public void onBitmapReady(@Nullable Bitmap bitmap) {
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                }
            };
            result.toBitmap(bmc);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    public void getDir(){
        File file = new File(getFilesDir().getAbsolutePath());
        File[] files = file.listFiles();
        String msg = (null != files ? files.length + " file(s) in a directory." : "files not found.");
        showToast("file directory absolute path: " + file.getAbsolutePath() + "\n" + msg);
    }


    /* ---------------------------------------------------------------------- */
    /* other method                                                           */
    /* ---------------------------------------------------------------------- */

    @Override
    public void updateProgress(int percent) {
        ((ProgressBar)findViewById(R.id.progressBar)).setProgress(percent);
        //tips:三項演算子
        // 「 条件式 ? trueReturn : falseReturn; 」
        String text = (10000 == percent) ? ("completed!") : (percent + "/10000");
        ((TextView)findViewById(R.id.textViewPercent)).setText(text);

        if (0 == percent || 10000 == percent) {
            String dateTime = ZonedDateTime.now().format(formatter);
            System.out.println("updateProgress: " + dateTime);
        }
    }

    @Override
    public void changeText(String message) {
        TextView textView = findViewById(R.id.textView);
        textView.setText(message);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

//////AsyncTaskLoaderのLoaderCallBacks実装//////////////////////////////////////////////////////////

    //tips:AsyncTaskLoader + Activity
    // Loaderの結果をonDestroyでoutStateに控えておく。
    // LoaderのインスタンスはLoadが終わっても消えないので、
    // onStartでチェックして
    // Loaderが終わってなかったなら(isStarted)initLoaderで再開(LoaderCallBacksと再紐づけ)。
    // Loaderが終わっていたら(isRunning)destroyLoaderで初期化する。
    private void checkLoader(){
        System.out.println("check loader.");
        AsyncTaskLoader loader = (AsyncTaskLoader) LoaderManager.getInstance(this).getLoader(LOADER_ID);
        if (null != loader && loader.isStarted()) {
            System.out.println("loader is started.");
            startLoader();
        }else if (null != loader && !((TestTaskLoader)loader).isRunning()){
            System.out.println("destroy loader.");
            LoaderManager.getInstance(this).destroyLoader(LOADER_ID);
            return;
        }else if (null == loader) System.out.println("loader is null.");
        if (null != loaderResult) {
            System.out.println("loader result is not null.");
            ((TextView)findViewById(R.id.textViewPercent)).setText(loaderResult);
        } else System.out.println("loader result is null.");
        System.out.println("check loader end.");
    }

    private void startLoader(){
        setLoaderCancelButton();
        Bundle arg = new Bundle();
        arg.putString("loaderArgument", "test");
        // isRunning(カスタム関数)で処理中かチェック
        LoaderManager manager = LoaderManager.getInstance(this);
        TestTaskLoader loader = (TestTaskLoader) manager.getLoader(LOADER_ID);
        if (null != loader && !loader.isRunning()){
            //キャンセル後とかに再開したいのでDestroy
            manager.destroyLoader(LOADER_ID);
        }
        // ここで渡したBundleはLoaderCallBacks::onCreateLoaderでLoaderのインスタンスを作るときに使うことができる
        manager.initLoader(LOADER_ID, arg, this);
    }

    private void setLoaderCancelButton(){
        AsyncTaskLoader loader = (AsyncTaskLoader) LoaderManager.getInstance(this).getLoader(LOADER_ID);
        if (null == loader) findViewById(R.id.button1).setOnClickListener(view -> cancelLoader());
    }

    //tips: AsyncTaskLoader
    // UIスレッドからAsyncTaskLoader#cancelLoad()をコール
    // 処理中のloadInBackGround()内でisLoadInBackgroundCanceled()をチェックしてbrake
    // onCancelled()でデータの破棄
    private void cancelLoader(){
        AsyncTaskLoader loader = (AsyncTaskLoader) LoaderManager.getInstance(this).getLoader(LOADER_ID);
        if (null == loader) return;
        Boolean cancelResult = null;
        try {
            cancelResult = loader.cancelLoad();
        }catch (OperationCanceledException e){
            e.printStackTrace();
        }

        showToast("キャンセルしました。cancelLoad() result: " + (null != cancelResult ? cancelResult : "null") );
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        loaderResult = null;
        System.out.println("onCreateLoader args id: " + id);
        String arg = "null";
        if (null != args) arg = args.getString("loaderArgument");

        return new TestTaskLoader<>(this, arg);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        showToast(data);
        loaderResult = data;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        //tips: loaderが保持しているデータが削除され、リセットされたときに呼ばれる。これ以降ローダーが持つデータへの参照はできない。
        System.out.println("onLoaderReset() called.");
    }

//////EventBus実装//////////////////////////////////////////////////////////////////////////////////

    //tips:EventBus
    // onResumeでregisterし、onPauseでunregisterを忘れない事。
    private void registerSubscribe(){
        EventBusService.noSubscriberEvent().register(this);
    }

    private void unregisterSubscribe(){
        EventBusService.noSubscriberEvent().unregister(this);
    }

    //tips:EventBus
    //Eventを受け取りたいメソッドに @Subscribe を装飾し、onStart辺りでregisterSubscribe(購読登録)。
    //デフォルトだとonEventはpostされた(ワーカー)スレッドで実行されるので、Fragment/Activityで
    // イベントを受け取る場合は (threadMode = ThreadMode.MAIN) でUIスレッドでの実行を指定すること。
    //時間がかかるネットワーク処理は ThreadMode.ASYNC(別のスレッド) を推奨。
    // (スレッド数には制限があるのでその場合数は考えること)
    @Subscribe (sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(TestLoaderProgressEvent event){
        if (event.msg().equals("testEvent")) updateProgress(event.value());
    }


    //onStopLoading()
    //Subclasses must implement this to take care of stopping their loader, as per stopLoading().  This is not called by clients directly, but as a result of a call to stopLoading().
    //This will always be called from the process's main thread.
    //
    //stopLoading()
    //This function will normally be called for you automatically by LoaderManager when the associated fragment/activity is being stopped.  When using a Loader with LoaderManager, you "must not" call this method yourself, or you will conflict with its management of the Loader.
    //Stops delivery of updates until the next time startLoading() is called.
    //Implementations should "not" invalidate their data at this point clients are still free to use the last data the loader reported.  They will, however, typically stop reporting new data if the data changes; they can still monitor for changes, but must not report them to the client until and if startLoading() is later called.
    //This updates the Loader's internal state so that isStarted() will return the correct value, and then calls the implementation's  onStopLoading().
    //Must be called from the process's main thread.
    //
    //onStartLoading()
    //Subclasses must implement this to take care of loading their data, as per startLoading().  This is not called by clients directly, but as a result of a call to startLoading().
    //This will always be called from the process's main thread.
    //
    //startLoading()
    //This function will normally be called for you automatically by LoaderManager when the associated fragment/activity is being started.  When using a Loader with LoaderManager, you "must not" call this method yourself, or you will conflict with its management of the Loader.
    //Starts an asynchronous load of the Loader's data. When the result is ready the callbacks will be called on the process's main thread.
    //If a previous load has been completed and is still valid the result may be passed to the callbacks immediately.
    //The loader will monitor the source of the data set and may deliver future callbacks if the source changes.
    //Calling stopLoading will stop the delivery of callbacks.
    //This updates the Loader's internal state so that isStarted() and isReset() will return the correct values, and then calls the implementation's onStartLoading().
    //Must be called from the process's main thread.
    //
    //onCanceled()
    //Called if the task was canceled before it was completed.  Gives the class a chance to clean up post-cancellation and to properly dispose of the result.
    //@param data: The value that was returned by loadInBackground, or null if the task threw OperationCanceledException.
    //
    //cancelLoadInBackGround()
    //Called on the main thread to abort a load in progress.
    //Override this method to abort the current invocation of loadInBackground that is running in the background on a worker thread.
    //This method should do nothing if loadInBackground has not started running or if it has already finished.
    //@see #loadInBackground
    //
    //cancelLoad()
    //Attempt to cancel the current load task.
    //Must be called on the main thread of the process.
    //Cancellation is not an immediate operation, since the load is performed in a background thread.  If there is currently a load in progress, this method requests that the load be canceled, and notes this is the case; once the background thread has completed its work its remaining state will be cleared.  If another load request comes in during this time, it will be held until the canceled load is complete.
    //@return: Returns "false" if the task could not be canceled, typically because it has already completed normally, or because startLoading() hasn't been called; returns "true" otherwise.  When "true" is returned, the task is still running and the OnLoadCanceledListener will be called when the task completes.
    //
    //onLoadCanceled()
    //Called on the thread that created the Loader when the load is canceled.
    //@param loader:  the loader that canceled the load.
    //
    //reset()
    //This function will normally be called for you automatically by LoaderManager when destroying a Loader.  When using a Loader with LoaderManager, you "must not" call this method yourself, or you will conflict with its management of the Loader.
    //Resets the state of the Loader.  The Loader should at this point free all of its resources, since it may never be called again; however, its startLoading() may later be called at which point it must be able to start running again.
    //This updates the Loader's internal state so that isStarted() and isReset() will return the correct values, and then calls the implementation's onReset().
    //Must be called from the process's main thread.

    //onStopLoading（）
    //サブクラスは、stopLoading（）に従ってローダーを停止するためにこれを実装する必要があります。これはクライアントによって直接呼び出されるのではなく、stopLoading（）の呼び出しの結果として呼び出されます。
    //これは常にプロセスのメインスレッドから呼び出されます。
    //
    //stopLoading（）
    //通常、この関数は、関連するフラグメント/アクティビティが停止されると、LoaderManagerによって自動的に呼び出されます。 LoaderManagerでLoaderを使用する場合、このメソッドを自分で呼び出さないでください。そうしないと、Loaderの管理と競合します。
    //次回startLoading（）が呼び出されるまで、更新の配信を停止します。
    //実装は、この時点でクライアントがデータを無効にしないようにする必要があります。クライアントは、ローダーが報告した最後のデータを自由に使用できます。ただし、通常、データが変更された場合、新しいデータのレポートは停止します。変更を引き続き監視できますが、後でstartLoading（）が呼び出されるまで、変更をクライアントに報告しないでください。
    //これにより、ローダーの内部状態が更新され、isStarted（）が正しい値を返し、実装のonStopLoading（）を呼び出します。
    //プロセスのメインスレッドから呼び出す必要があります。
    //
    //onStartLoading（）
    //サブクラスは、startLoading（）に従ってデータをロードするためにこれを実装する必要があります。これはクライアントによって直接呼び出されるのではなく、startLoading（）の呼び出しの結果として呼び出されます。
    //これは常にプロセスのメインスレッドから呼び出されます。
    //
    //startLoading（）
    //通常、この関数は、関連するフラグメント/アクティビティが開始されるときに、LoaderManagerによって自動的に呼び出されます。 LoaderManagerでLoaderを使用する場合、このメソッドを自分で呼び出さないでください。そうしないと、Loaderの管理と競合します。
    //ローダーのデータの非同期ロードを開始します。結果の準備ができると、プロセスのメインスレッドでコールバックが呼び出されます。
    //以前のロードが完了し、まだ有効である場合、結果はすぐにコールバックに渡されます。
    //ローダーはデータセットのソースを監視し、ソースが変更された場合、将来のコールバックを配信する場合があります。
    //stopLoadingを呼び出すと、コールバックの配信が停止します。
    //これにより、ローダーの内部状態が更新され、isStarted（）およびisReset（）が正しい値を返し、実装のonStartLoading（）を呼び出します。
    //プロセスのメインスレッドから呼び出す必要があります。
    //
    //onCanceled（）
    //タスクが完了する前にキャンセルされた場合に呼び出されます。クラスにキャンセル後をクリーンアップし、結果を適切に破棄する機会を与えます。
    //@param data：loadInBackgroundによって返された値、またはタスクがOperationCanceledExceptionをスローした場合はnull。
    //
    //cancelLoadInBackGround（）
    //進行中のロードを中止するためにメインスレッドで呼び出されます。
    //このメソッドをオーバーライドして、ワーカースレッドのバックグラウンドで実行されているloadInBackgroundの現在の呼び出しを中止します。
    //loadInBackgroundの実行が開始されていない場合、またはすでに終了している場合、このメソッドは何も実行しません。
    //@see #loadInBackground
    //
    //cancelLoad（）
    //現在の読み込みタスクをキャンセルしようとしました。
    //プロセスのメインスレッドで呼び出す必要があります。
    //ロードはバックグラウンドスレッドで実行されるため、キャンセルは即時の操作ではありません。
    // 現在進行中のロードがある場合、このメソッドはロードのキャンセルを要求し、
    // これが事実であることに注意してください。バックグラウンドスレッドが作業を完了すると、
    // 残りの状態はクリアされます。この間に別のロードリクエストが着信すると、
    // キャンセルされたロードが完了するまで保留されます。
    //@return：タスクをキャンセルできなかった場合、通常は既に正常に完了したか、
    // startLoading（）が呼び出されなかったため、「false」を返します。
    // それ以外の場合は「true」を返します。 「true」が返された場合、タスクはまだ実行中であり、
    // タスクが完了するとOnLoadCanceledListenerが呼び出されます。
    //
    //onLoadCanceled（）
    //ロードがキャンセルされたときにローダーを作成したスレッドで呼び出されます。
    //@param loader：ロードをキャンセルしたローダー。
    //
    //reset（）
    //この関数は通常、Loaderを破棄するときにLoaderManagerによって自動的に呼び出されます。 LoaderManagerでLoaderを使用する場合、このメソッドを自分で呼び出さないでください。そうしないと、Loaderの管理と競合します。
    //ローダーの状態をリセットします。ローダーは、再び呼び出されることはないため、この時点ですべてのリソースを解放する必要があります。ただし、後でstartLoading（）が呼び出され、その時点で再び実行を開始できる必要があります。
    //これにより、isStarted（）およびisReset（）が正しい値を返すようにローダーの内部状態が更新され、実装のonReset（）が呼び出されます。
    //プロセスのメインスレッドから呼び出す必要があります。
}