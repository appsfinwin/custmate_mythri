package com.finwin.mythri.custmate.mpin_register;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.finwin.mythri.custmate.R;
import com.finwin.mythri.custmate.SupportingClass.ConstantClass;
import com.finwin.mythri.custmate.SupportingClass.Enc_Utils;
import com.finwin.mythri.custmate.SupportingClass.Enc_crypter;
import com.finwin.mythri.custmate.home.home_fragment.HomeFragment;
import com.finwin.mythri.custmate.mpin_register.action.MpinRegisterAction;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MPINRegisterFragment extends Fragment {

    Button submit, finish;
    //    PasswordView mpin1, mpin2;
    AppCompatEditText mpin1, mpin2;
    Context context;
    RequestQueue requestQueue;
    final Enc_crypter encr = new Enc_crypter();
    String demessage;
    Dialog dialogMpin;
    MpinRegisterViewmodel viewmodel;
    SweetAlertDialog sweetAlertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        requestQueue = Volley.newRequestQueue(context);
        sharedPreferences= getActivity().getSharedPreferences("com.finwin.mythri.custmate",Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewmodel=new ViewModelProvider(getActivity()).get(MpinRegisterViewmodel.class);
        return inflater.inflate(R.layout.fragment_mpinregister, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mpin1 = view.findViewById(R.id.et_mpin_1);
        mpin2 = view.findViewById(R.id.et_mpin_2);
        submit = view.findViewById(R.id.btn_mpin_submit);
        finish = view.findViewById(R.id.btn_mpin_finish);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
               // assert fragmentManager != null;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                HomeFragment containerFragment = new HomeFragment();
                fragmentTransaction.replace(R.id.frame_layout, containerFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        viewmodel.getmAction().observe(getViewLifecycleOwner(), new Observer<MpinRegisterAction>() {
            @Override
            public void onChanged(MpinRegisterAction mpinRegisterAction) {
                switch (mpinRegisterAction.getAction())
                {
                    case MpinRegisterAction.LOGIN_SUCCESS:

                        dialogMpin.dismiss();
                        registerMpin();
                        break;

                        case MpinRegisterAction.MPIN_REGISTER_SUCCESS:

                            sweetAlertDialog.dismiss();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("SUCCESS")
                                    .setContentText("MPIN Registered")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            FragmentManager fragmentManager = getFragmentManager();
                                            // assert fragmentManager != null;
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            HomeFragment containerFragment = new HomeFragment();
                                            fragmentTransaction.replace(R.id.frame_layout_login, containerFragment);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();
                                        }
                                    })
                                    .show();
                            mpin1.setEnabled(false);
                            mpin2.setEnabled(false);
                            submit.setEnabled(false);
                            break;

                            case MpinRegisterAction.API_ERROR:

                    case MpinRegisterAction.MPIN_REGISTER_ERROR:

                        sweetAlertDialog.dismiss();
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR")
                                        .setContentText(mpinRegisterAction.getError())
                                        .show();
                                break;
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mpin1.getText().toString().equals(mpin2.getText().toString())) {
                    if (mpin1.getText().toString().length() == 6) {
                        dialogMpin = new Dialog(context);
                        dialogMpin.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogMpin.setCancelable(true);
                        dialogMpin.setContentView(R.layout.validation);
                        final Button validate = dialogMpin.findViewById(R.id.btn_validate);
                        final EditText username = dialogMpin.findViewById(R.id.et_username);
                        final EditText password = dialogMpin.findViewById(R.id.et_password);
                        validate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (username.getText().toString().equals("") && password.getText().toString().equals("")) {
                                    Toast.makeText(getActivity(), "Username & Password Must not be Null", Toast.LENGTH_LONG).show();
                                } else {
                                    validateUser(username.getText().toString(), password.getText().toString());
                                }
                            }
                        });
                        dialogMpin.show();
                    } else {
                        Toast.makeText(getActivity(), "MPIN Should Be 6 Digits", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "MPIN do not match!", Toast.LENGTH_LONG).show();
                }

            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    public void validateUser(String username, String password) {
        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        items.put("username", username);
        items.put("password", password);

        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));
        viewmodel.validateUser(params);

    }

    public void registerMpin() {

        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("Please wait")
                .show();
        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        items.put("userid", sharedPreferences.getString(ConstantClass.custId,""));
        items.put("MPIN", mpin2.getText().toString());

        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));
        viewmodel.registerMpin(params);

    }

//    private void validateUser(String _username, String _password, final Dialog simpleDialog) {
//        final String username = _username;
//        final String password = _password;
//        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//        sweetAlertDialog.setTitleText("Please wait")
//                .show();
////        String url = ip_global + "/custappUserLogin";
//
//        StringRequest postRequest = new StringRequest(Request.Method.POST, api_custappUserLogin,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        try {
//                            JSONObject jsonFrstRespns = new JSONObject(response);
//                            if (jsonFrstRespns.has("data")) {
//                                demessage = decValues(encr.revDecString(jsonFrstRespns.getString("data")));
//                                Log.e("demessage", demessage);
//                            }
//                            sweetAlertDialog.dismissWithAnimation();
//                            JSONObject jsonResponse = new JSONObject(demessage);
//                            if (jsonResponse.getJSONObject("user").has("error")) {
//                                Toast.makeText(context, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
//                            } else {
//                                try {
//                                    sweetAlertDialog.dismissWithAnimation();
//                                    simpleDialog.dismiss();
//                                    validateMpin();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    sweetAlertDialog.dismissWithAnimation();
//                                    Toast.makeText(context, "Some Error Occurred!", Toast.LENGTH_SHORT).show();
//                                    ErrorLog.submitError(getActivity(), this.getClass().getSimpleName() + ":" + new Object() {
//                                    }.getClass().getEnclosingMethod().getName(), e.toString());
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            sweetAlertDialog.dismissWithAnimation();
//                            Toast.makeText(context, "Some Error Occurred!", Toast.LENGTH_SHORT).show();
//                            ErrorLog.submitError(getActivity(), this.getClass().getSimpleName() + ":" + new Object() {
//                            }.getClass().getEnclosingMethod().getName(), e.toString());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            sweetAlertDialog.dismissWithAnimation();
//                            Toast.makeText(context, "Some Error Occurred!", Toast.LENGTH_SHORT).show();
//                            ErrorLog.submitError(getActivity(), this.getClass().getSimpleName() + ":" + new Object() {
//                            }.getClass().getEnclosingMethod().getName(), e.toString());
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                sweetAlertDialog.dismissWithAnimation();
//                Toast.makeText(context, "Some Error Occurred!", Toast.LENGTH_SHORT).show();
//                ErrorLog.submitError(getActivity(), this.getClass().getSimpleName() + ":" + new Object() {
//                }.getClass().getEnclosingMethod().getName(), error.toString());
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                // the POST parameters:
//                Map<String, String> params = new HashMap<>();
//                Map<String, String> items = new HashMap<>();
//                items.put("username", username);
//                items.put("password", password);
//
//                params.put("data", encr.conRevString(Enc_Utils.enValues(items)));
//                return params;
//            }
//        };
//        requestQueue.add(postRequest);
//    }

//    private void validateMpin() {
//        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//        sweetAlertDialog.setTitleText("Please wait")
//                .show();
////        String url = ip_global + "/registerMPIN";
//
//        StringRequest postRequest = new StringRequest(Request.Method.POST, api_registerMPIN,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        try {
//                            JSONObject jsonFrstRespns = new JSONObject(response);
//                            if (jsonFrstRespns.has("data")) {
//                                demessage = decValues(encr.revDecString(jsonFrstRespns.getString("data")));
//                                Log.e("demessage", demessage);
//
//                                sweetAlertDialog.dismissWithAnimation();
//                                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
//                                        .setTitleText("SUCCESS")
//                                        .setContentText("MPIN Registered")
//                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                            @Override
//                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                                FragmentManager fragmentManager = getFragmentManager();
//                                                // assert fragmentManager != null;
//                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                                HomeFragment containerFragment = new HomeFragment();
//                                                fragmentTransaction.replace(R.id.frame_layout_login, containerFragment);
//                                                fragmentTransaction.addToBackStack(null);
//                                                fragmentTransaction.commit();
//                                            }
//                                        })
//                                        .show();
//                                mpin1.setEnabled(false);
//                                mpin2.setEnabled(false);
//                                submit.setEnabled(false);
//                                //finish.setVisibility(View.VISIBLE);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            sweetAlertDialog.dismissWithAnimation();
//                            Toast.makeText(context, "Some Error Occurred!", Toast.LENGTH_SHORT).show();
//                            ErrorLog.submitError(getActivity(), this.getClass().getSimpleName() + ":" + new Object() {
//                            }.getClass().getEnclosingMethod().getName(), e.toString());
//                        }
//                        Log.d("", response.toString());
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                sweetAlertDialog.dismissWithAnimation();
//                Toast.makeText(context, "Some Error Occurred!", Toast.LENGTH_SHORT).show();
//                ErrorLog.submitError(getActivity(), this.getClass().getSimpleName() + ":" + new Object() {
//                }.getClass().getEnclosingMethod().getName(), error.toString());
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                // the POST parameters:
//                Map<String, String> params = new HashMap<>();
//                Map<String, String> items = new HashMap<>();
//                items.put("userid", ConstantClass.const_cusId);
//                items.put("MPIN", mpin2.getText().toString());
//
//                params.put("data", encr.conRevString(Enc_Utils.enValues(items)));
//                return params;
//            }
//        };
//        requestQueue.add(postRequest);
//    }

}
