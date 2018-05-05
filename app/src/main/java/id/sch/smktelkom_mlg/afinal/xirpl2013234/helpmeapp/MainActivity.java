package id.sch.smktelkom_mlg.afinal.xirpl2013234.helpmeapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import id.sch.smktelkom_mlg.afinal.xirpl2013234.helpmeapp.Common.Common;
import id.sch.smktelkom_mlg.afinal.xirpl2013234.helpmeapp.Model.Rider;
import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    Button btnSignIn;
    TextView btnRegister;
    ConstraintLayout rootLayout;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    TextView txt_forgot_pwd;


    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_main);

        //init paper db
        Paper.init(this);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //inisiasi firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference(Common.user_driver_tbl);

        //inisiasi tampilan
        rootLayout = findViewById(R.id.rootLayout);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);
        txt_forgot_pwd = findViewById(R.id.txt_forgot_password);
        txt_forgot_pwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showDioalogForgotPwd();
                return false;
            }
        });

        //Event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });

        String user = Paper.book().read(Common.user_field);
        String pwd = Paper.book().read(Common.pwd_field);

        if (user != null && pwd != null) {
            if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pwd)) {
                autoLogin(user, pwd);
            }
        }
    }

    private void autoLogin(String user, String pwd) {

        final android.app.AlertDialog waitingDialog = new SpotsDialog(MainActivity.this);
        waitingDialog.show();

        //login
        auth.signInWithEmailAndPassword(user, pwd)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        waitingDialog.dismiss();

                        FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Common.currentUser = dataSnapshot.getValue(Rider.class);
                                        startActivity(new Intent(MainActivity.this, Home.class));
                                        waitingDialog.dismiss();
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        waitingDialog.dismiss();
                        Snackbar.make(rootLayout, "Failed" + e.getMessage(), Snackbar.LENGTH_SHORT)
                                .show();

                        btnSignIn.setEnabled(true);
                    }
                });
    }

    private void showDioalogForgotPwd() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Lupa Password");
        alertDialog.setMessage("Masukan Alamat Email");

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View forgot_pwd_layout = inflater.inflate(R.layout.layout_forgot_pwd, null);

        final MaterialEditText edtEmail = forgot_pwd_layout.findViewById(R.id.edtEmail);
        alertDialog.setView(forgot_pwd_layout);

        alertDialog.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                final SpotsDialog waitingDialog = new SpotsDialog(MainActivity.this);
                waitingDialog.show();

                auth.sendPasswordResetEmail(edtEmail.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                waitingDialog.dismiss();

                                Snackbar.make(rootLayout, "Link untuk Reset Password Telah Dikirim melalui Email", Snackbar.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        waitingDialog.dismiss();

                        Snackbar.make(rootLayout, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showLoginDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN ");
        dialog.setMessage("Please use email to sign in");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login, null);

        final MaterialEditText edtEmail = login_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = login_layout.findViewById(R.id.edtPassword);


        dialog.setView(login_layout);

        //set Button
        dialog.setPositiveButton("SIGN IN ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                btnSignIn.setEnabled(false);

                //Cek Validasi
                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter email addres", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter password", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (edtPassword.getText().toString().length() < 6) {
                    Snackbar.make(rootLayout, "Password too short !!!", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                final SpotsDialog waitingDialog = new SpotsDialog(MainActivity.this);
                waitingDialog.show();

                //login
                auth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                waitingDialog.dismiss();

                                FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl)
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                Paper.book().write(Common.user_field, edtEmail.getText().toString());
                                                Paper.book().write(Common.pwd_field, edtPassword.getText().toString());

                                                Common.currentUser = dataSnapshot.getValue(Rider.class);
                                                startActivity(new Intent(MainActivity.this, Home.class));
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                waitingDialog.dismiss();
                                Snackbar.make(rootLayout, "Failed" + e.getMessage(), Snackbar.LENGTH_SHORT)
                                        .show();

                                btnSignIn.setEnabled(true);
                            }
                        });
            }
        });

        dialog.setNegativeButton("CANCEL ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    private void showRegisterDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register, null);

        final MaterialEditText edtEmail = register_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = register_layout.findViewById(R.id.edtPassword);
        final MaterialEditText edtNama = register_layout.findViewById(R.id.edtNama);
        final MaterialEditText edtPhone = register_layout.findViewById(R.id.edtPhone);
        final MaterialEditText edtUmur = register_layout.findViewById(R.id.edtUmur);
        final MaterialEditText edtAlamat = register_layout.findViewById(R.id.edtAlamat);
        final MaterialEditText edtJenisKelamin = register_layout.findViewById(R.id.edtJenisKelamin);
        final MaterialEditText edtKK = register_layout.findViewById(R.id.edtKK);
        final MaterialEditText edtKTP = register_layout.findViewById(R.id.edtKTP);
        final ImageView image_upload = register_layout.findViewById(R.id.image_upload);
        image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseImage();
            }
        });


        new AlertDialog.Builder(MainActivity.this).setView(register_layout)
                .setTitle("REGISTER ")
                .setPositiveButton("REGISTER ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        //Cek Validasi
                        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                            Snackbar.make(rootLayout, "Please enter email addres", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                            Snackbar.make(rootLayout, "Please enter phone number", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                            Snackbar.make(rootLayout, "Please enter password", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        if (edtPassword.getText().toString().length() < 6) {
                            Snackbar.make(rootLayout, "Password too short !!!", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        //Register new users
                        auth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        //save users db
                                        Rider rider = new Rider();
                                        rider.setEmail(edtEmail.getText().toString());
                                        rider.setNama(edtNama.getText().toString());
                                        rider.setPassword(edtPassword.getText().toString());
                                        rider.setPhone(edtPhone.getText().toString());
                                        rider.setUmur(edtUmur.getText().toString());
                                        rider.setAlamat(edtAlamat.getText().toString());
                                        rider.setJenisKelamin(edtJenisKelamin.getText().toString());
                                        rider.setKK(edtKK.getText().toString());
                                        rider.setKTP(edtKTP.getText().toString());

                                        //use email to key
                                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(rider)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Snackbar.make(rootLayout, "Register success fully ", Snackbar.LENGTH_SHORT)
                                                                .show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Snackbar.make(rootLayout, "Failed " + e.getMessage(), Snackbar.LENGTH_SHORT)
                                                                .show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(rootLayout, "Failed " + e.getMessage(), Snackbar.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                    }
                })

                .setNegativeButton("CANCEL ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }


    private void choseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Common.PICK_IMAGE_REQUEST);
    }


}
