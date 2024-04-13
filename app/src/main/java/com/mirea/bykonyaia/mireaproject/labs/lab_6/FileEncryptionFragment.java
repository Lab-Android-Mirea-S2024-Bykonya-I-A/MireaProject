package com.mirea.bykonyaia.mireaproject.labs.lab_6;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mirea.bykonyaia.mireaproject.R;
import com.mirea.bykonyaia.mireaproject.databinding.FragmentFileEncryptionBinding;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FileEncryptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileEncryptionFragment extends Fragment {
    private FragmentFileEncryptionBinding binding = null;

    public FileEncryptionFragment() { }
    public static FileEncryptionFragment newInstance() {
        FileEncryptionFragment fragment = new FileEncryptionFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFileEncryptionBinding.inflate(inflater, container, false);
        binding.saveFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { OnSaveInfoButtonPressed(view); }
        });
        binding.loadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { OnLoadInfoButtonPressed(view); }
        });

        return binding.getRoot();
    }

    public void OnSaveInfoButtonPressed(View v) {
        if(PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new	String[] {
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },	200);
            return;
        }

        try {
            final SecretKey password = getSecretKey(binding.passwordInput.getText().toString());
            final String dectyptedText = binding.fileTextEdit.getText().toString();
            final byte[] encryptedText = encryptMsg(password, dectyptedText);

            Toast.makeText(getActivity(), String.format("[%s][%s]", binding.fileTextEdit.getText().toString(), new String(encryptedText)), Toast.LENGTH_SHORT).show();
            final File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            final File file = new File(directory, binding.filenameInput.getText().toString());
            final FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsoluteFile());
            fileOutputStream.write(encryptedText);
            fileOutputStream.close();

            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), String.format("Error: %s", e.toString()), Toast.LENGTH_LONG).show();
        }
    }
    public void OnLoadInfoButtonPressed(View v) {
        if(PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new	String[] {
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },	200);
            return;
        }

        try {
            final File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            final File file = new File(directory, binding.filenameInput.getText().toString());
            final BufferedInputStream input = new BufferedInputStream(new FileInputStream(file.getAbsoluteFile()));
            final byte[] encryptedText = new byte[(int)file.length()];
            input.read(encryptedText, 0, encryptedText.length);
            input.close();

            final SecretKey password = getSecretKey(binding.passwordInput.getText().toString());
            final String decryptedText = decryptMsg(password, encryptedText);
            binding.fileTextEdit.setText(decryptedText);
            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), String.format("Error: %s", e.toString()), Toast.LENGTH_LONG).show();
        }
    }

    static private byte[] encryptMsg(SecretKey secret, String message) {
        try {
            final Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            return cipher.doFinal(message.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }
    static private String decryptMsg(SecretKey secret, byte[] cipherText){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            return new String(cipher.doFinal(cipherText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException| IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
    static private SecretKey getSecretKey(String password) {
        try {
            final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            final KeySpec spec = new PBEKeySpec(password.toCharArray(), password.getBytes(), 10000, 128);
            final SecretKey tmp = factory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(), "AES");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}