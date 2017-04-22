package com.fei.textsearch.Chooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.fei.textsearch.R;

/**
 * Created by fei on 4/20/2017.
 */
public class FileChooser extends ListActivity {

    private File currentDir;
    private FileArrayAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDir = new File("/");
        if (Environment.getExternalStorageState().
                equals(Environment.MEDIA_MOUNTED)) {
            System.out.println("sdcard ready");
            currentDir = Environment.getExternalStorageDirectory();
        }
        fill(currentDir);
    }

    private void fill(File f) {
        File[] dirs = f.listFiles();
        this.setTitle("Current Dir: " + f.getName());
        List<Option> dir = new ArrayList<>();
        dir.add(0, new Option(".. [Pick current folder]", "done", f.getParent()));
        List<Option> fls = new ArrayList<>();
        try {
            for (File ff : dirs) {
                if (ff.isDirectory())
                    dir.add(new Option(ff.getName(), "Folder", ff.getAbsolutePath()));
                else {
                    fls.add(new Option(ff.getName(), "File Size: " + ff.length(), ff.getAbsolutePath()));
                }
            }
        } catch (Exception e) {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if (f.getParent() != null)
            dir.add(0, new Option("..", "Parent Directory", f.getParent()));
        adapter = new FileArrayAdapter(FileChooser.this, R.layout.file_view, dir);
        this.setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Option o = adapter.getItem(position);
        String data = o.getData().toLowerCase();
        switch (data) {
            case "done":
                done();
                break;
            case "folder":
            case "parent directory":
                currentDir = new File(o.getPath());
                fill(currentDir);
                break;
            default:
                onFileClick(o);
                break;
        }
    }

    private void onFileClick(Option o) {
        Toast.makeText(this, "File Clicked: " + o.getName(), Toast.LENGTH_SHORT).show();
    }

    private void done() {
        Intent intent = new Intent();
        intent.putExtra("editTextValue", currentDir.getPath());
        setResult(RESULT_OK, intent);
        finish();
    }
}

