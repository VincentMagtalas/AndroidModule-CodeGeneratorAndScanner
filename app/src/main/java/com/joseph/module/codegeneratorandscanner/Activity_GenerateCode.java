package com.joseph.module.codegeneratorandscanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class Activity_GenerateCode extends AppCompatActivity {

    ImageView imageView;
    Button button;
    EditText editText,etInputType;
    String EditTextValue,CodeType ;
    Thread thread ;
    public final static int QRcodeWidth = 500 ;
    public final static int BarCodeWidth = 500 ;
    public final static int BarCodeHeight = 250 ;
    Bitmap bitmap ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_code);

        imageView = (ImageView)findViewById(R.id.imageView);
        editText = (EditText)findViewById(R.id.editText);
        etInputType = (EditText)findViewById(R.id.etInputType);
        button = (Button)findViewById(R.id.button);

        etInputType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertMerchant(view);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditTextValue = editText.getText().toString();

                if(editText.getText().toString().equals("") || etInputType.getText().toString().equals("")){
                    Toast.makeText(Activity_GenerateCode.this,"Please fill-up form",Toast.LENGTH_LONG).show();
                }else{
                    AsyncGenerate task = new AsyncGenerate(Activity_GenerateCode.this);
                    task.execute();
                }

            }
        });

    }

    private class AsyncGenerate extends AsyncTask<String, Void, Void> {

        Context ctx;
        ProgressDialog progDailog;

        AsyncGenerate(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            Log.i("XOXO", "onPreExecute");

            progDailog = new ProgressDialog(ctx);
            progDailog.setMessage("Generating...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i("XOXO", "doInBackground");
            try {
                bitmap = TextToImageEncode(EditTextValue);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("XOXO", "onPostExecute");
            progDailog.dismiss();

            imageView.setImageBitmap(bitmap);


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("XOXO", "onProgressUpdate");
        }

    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix = null;
        try {
            switch (CodeType) {
                case "UPC-A":
                    bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.UPC_A, BarCodeHeight, BarCodeWidth, null);
                    break;
                case "UPC-E":
                    bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.UPC_E, QRcodeWidth, QRcodeWidth, null);
                    break;
                case "EAN-8":
                    bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.EAN_8, QRcodeWidth, QRcodeWidth, null);
                    break;
                case "EAN-13":
                    bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.EAN_13, QRcodeWidth, QRcodeWidth, null);
                    break;
                case "Code 39":
                    bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.CODE_39, QRcodeWidth, QRcodeWidth, null);
                    break;
                case "Code 93":
                    bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.CODE_93, QRcodeWidth, QRcodeWidth, null);
                    break;
                case "Code 128":
                    bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.CODE_128, BarCodeWidth, BarCodeHeight, null);
                    break;
                case "Codabar":
                    bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.CODABAR, QRcodeWidth, QRcodeWidth, null);
                    break;
                case "ITF":
                    bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.ITF, QRcodeWidth, QRcodeWidth, null);
                    break;
                case "RSS-14":
                    bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.RSS_14, QRcodeWidth, QRcodeWidth, null);
                    break;
                case "RSS-Expanded":
                    bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.RSS_EXPANDED, QRcodeWidth, QRcodeWidth, null);
                    break;
                case "QR Code":
                    bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.QR_CODE, QRcodeWidth, QRcodeWidth, null);
                    break;
                case "Data Matrix":
                    bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.DATA_MATRIX, QRcodeWidth, QRcodeWidth, null);
                    break;
                default:
                    bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.CODE_128, QRcodeWidth, QRcodeWidth, null);
            }



        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.colorBlack):getResources().getColor(R.color.colorWhite);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    private void alertMerchant(View v){

        final String[] choices = new String[2];
        //choices[0] = "UPC-A";
        //choices[1] = "UPC-E";
        //choices[2] = "EAN-8";
        //choices[3] = "EAN-13";
        //choices[4] = "Code 39";
        //choices[5] = "Code 93";
        choices[0] = "Code 128";
        //choices[7] = "Codabar";
        //choices[8] = "ITF";
        //choices[9] = "RSS-14";
        //choices[10] = "RSS-Expanded";
        choices[1] = "QR Code";
        //choices[12] = "Data Matrix";


        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Select Code Type");
        builder.setItems(choices, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                CodeType = (choices[item]);
                etInputType.setText(choices[item]);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
