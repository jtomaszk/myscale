package com.jtomaszk.apps.myscale.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.jtomaszk.apps.common.logger.EidLogger;
import com.jtomaszk.apps.myscale.R;
import com.jtomaszk.apps.myscale.dao.WeightEntryDaoImpl;
import com.jtomaszk.apps.myscale.entity.WeightEntry;
import com.jtomaszk.apps.myscale.importer.Importer;
import com.jtomaszk.apps.myscale.importer.ImporterService;

import java.io.File;
import java.util.List;

public class ImportActivity extends Activity {

    private static final EidLogger logger = EidLogger.getLogger();

    private static final int PICKFILE_RESULT_CODE = 1;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        Intent target = new Intent(Intent.ACTION_GET_CONTENT);
        // The MIME data type filter
        target.setType("*/*");
        // Only return URIs that can be opened with ContentResolver
        target.addCategory(Intent.CATEGORY_OPENABLE);

        verifyStoragePermissions(this);

        Intent intent = Intent.createChooser(
                target, "Aa");
        startActivityForResult(intent, PICKFILE_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    String filePath = data.getData().getPath();
                    logger.i("20160123:160043", "filePath %s", filePath);

                    File file = new File(filePath);
                    Importer importer = new Importer();
                    List<WeightEntry> list = importer.parseCsvData(file);

                    ImporterService importerService = new ImporterService();
                    importerService.setDao(new WeightEntryDaoImpl());
                    importerService.mergeFromImport(list);
                    finish();
                }
                break;

        }
    }
}
