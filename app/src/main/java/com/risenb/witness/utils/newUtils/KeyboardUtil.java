package com.risenb.witness.utils.newUtils;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;

public class KeyboardUtil {
    private KeyboardView keyboardView;
    private Keyboard keyboard;
    private EditText editText;
    private int primaryCode;
    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        public void swipeUp() {
        }

        public void swipeRight() {
        }

        public void swipeLeft() {
        }

        public void swipeDown() {
        }

        public void onText(CharSequence text) {
        }

        public void onRelease(int primaryCode) {
        }

        public void onPress(int primaryCode) {
        }

        public void onKey(int primaryCode, int[] keyCodes) {
            KeyboardUtil.this.primaryCode = primaryCode;
            Editable editable = KeyboardUtil.this.editText.getText();
            int start = KeyboardUtil.this.editText.getSelectionStart();
            if (primaryCode != -3) {
                if (primaryCode == -5) {
                    if (editable != null && editable.length() > 0 && start > 0) {
                        editable.delete(start - 1, start);
                    }
                } else if (primaryCode == '\ue04b') {
                    if (start > 0) {
                        KeyboardUtil.this.editText.setSelection(start - 1);
                    }
                } else if (primaryCode == '\ue04d') {
                    if (start < KeyboardUtil.this.editText.length()) {
                        KeyboardUtil.this.editText.setSelection(start + 1);
                    }
                } else {
                    editable.insert(start, Character.toString((char) primaryCode));
                }
            }

        }
    };

    public KeyboardUtil(final Activity activity, final EditText editText, KeyboardView keyboardView) {
        this.editText = editText;
        this.keyboardView = keyboardView;
        if (Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(0);
        } else {
            activity.getWindow().setSoftInputMode(3);

            try {
                Class e = EditText.class;
                Method setShowSoftInputOnFocus = e.getMethod("setShowSoftInputOnFocus", new Class[]{Boolean.TYPE});
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, new Object[]{Boolean.valueOf(false)});
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }

        editText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService("input_method");
                imm.showSoftInput(editText, 2);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                KeyboardUtil.this.showKeyboard();
                return false;
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean bl) {
                if (!bl) {
                    KeyboardUtil.this.hideKeyboard();
                } else {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService("input_method");
                    imm.showSoftInput(editText, 2);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    KeyboardUtil.this.showKeyboard();
                }

            }
        });
        this.keyboard = new Keyboard(activity, IDcardUtils.getIdCardUtils().symbols);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(true);
        keyboardView.setOnKeyboardActionListener(this.listener);
        keyboardView.setKeyboard(this.keyboard);
        keyboardView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1 && KeyboardUtil.this.primaryCode == -3) {
                    KeyboardUtil.this.hideKeyboard();
                }

                return false;
            }
        });
    }

    public void showKeyboard() {
        int visibility = this.keyboardView.getVisibility();
        if (visibility == 8 || visibility == 4) {
            this.keyboardView.setVisibility(View.VISIBLE);
        }

    }

    public void hideKeyboard() {
        int visibility = this.keyboardView.getVisibility();
        if (visibility == 0) {
            this.keyboardView.setVisibility(View.GONE);
        }

    }

    public boolean checkNum(String cardNo) {
        if (TextUtils.isEmpty(cardNo)) {
            return false;
        } else if (cardNo.length() < 18) {
            return false;
        } else {
            int[] intArr = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            int sum = 0;

            int mod;
            for (mod = 0; mod < intArr.length; ++mod) {
                sum += Character.digit(cardNo.charAt(mod), 10) * intArr[mod];
            }

            mod = sum % 11;
            int[] intArr2 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            int[] intArr3 = new int[]{1, 0, 88, 9, 8, 7, 6, 5, 4, 3, 2};
            String matchDigit = "";

            for (int i = 0; i < intArr2.length; ++i) {
                int j = intArr2[i];
                if (j == mod) {
                    matchDigit = String.valueOf(intArr3[i]);
                    if (intArr3[i] > 57) {
                        matchDigit = String.valueOf((char) intArr3[i]);
                    }
                }
            }

            if (matchDigit.equals(cardNo.substring(cardNo.length() - 1))) {
                return true;
            } else {
                return false;
            }
        }
    }
}
