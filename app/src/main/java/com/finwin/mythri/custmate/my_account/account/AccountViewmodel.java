package com.finwin.mythri.custmate.my_account.account;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.AndroidViewModel;

import com.finwin.mythri.custmate.BR;
import com.finwin.mythri.custmate.SupportingClass.ConstantClass;
import com.finwin.mythri.custmate.databinding.FrgMyAccountBinding;

public class AccountViewmodel extends AndroidViewModel implements Observable {
    public AccountViewmodel(@NonNull Application application) {
        super(application);
        setAccountNumbers();
        sharedPreferences=application.getSharedPreferences("com.finwin.mythri.custmate",Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
    }
    private PropertyChangeRegistry registry = new PropertyChangeRegistry();
    public ObservableArrayList<String> listAccountNumbers=new ObservableArrayList<>();
    public ObservableField<String> acountNumberSelected=new ObservableField<>("");
    private int selectedAccountNumber;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Bindable
    public int getSelectedAccountNumber() {
        return selectedAccountNumber;
    }
    private void setAccountNumbers() {
        listAccountNumbers.clear();
        for (int i = 0; i< ConstantClass.listAccountNumbers.size(); i++)
        {
            if ( ConstantClass.listScheme.get(i).contains("[FD]")){
                listAccountNumbers.add(ConstantClass.listAccountNumbers.get(i)+ "(FD)");
            }else if ( ConstantClass.listScheme.get(i).contains("[RD]")){
                listAccountNumbers.add(ConstantClass.listAccountNumbers.get(i)+ "(RD)");
            }else if ( ConstantClass.listScheme.get(i).contains("[SB]")){
                listAccountNumbers.add(ConstantClass.listAccountNumbers.get(i)+ "(SB)");
            }

        }

    }

//    private void setAccountNumbers() {
//        for (String a : ConstantClass.listAccountNumbers) {
//            listAccountNumbers.add(a);
//        }
//    }
    public void setSelectedAccountNumber(int selectedAccountNumber) {
        this.selectedAccountNumber = selectedAccountNumber;
        registry.notifyChange(this, BR.selectedAccountNumber);
    }
    public void onSelectedAccountNumber(AdapterView<?> parent, View view, int position, long id)
    {
        editor.putString(ConstantClass.SCHEME,ConstantClass.listScheme.get(position));
        editor.putString(ConstantClass.accountNumber,ConstantClass.listAccountNumbers.get(position));
        editor.commit();
        //ConstantClass.const_accountNumber = ConstantClass.listAccountNumbers.get(position);
        binding.tvAccNo.setText(ConstantClass.listAccountNumbers.get(position));
    }
    FrgMyAccountBinding binding;
    public void setBinding(FrgMyAccountBinding binding) {
        this.binding= binding;
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.remove(callback);
    }
}