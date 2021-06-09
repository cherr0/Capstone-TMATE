package com.tmate.user.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.R;
import com.tmate.user.data.Dv_option;
import com.tmate.user.net.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreferenceFragment extends Fragment {

    private View view;
    private ImageView btn_back_preference;

    Switch fp_dohurry;
    Switch fp_donavi;
    Switch fp_doquiet;
    Switch fp_doanimal;
    Switch fp_doload;
    Switch fp_dochild;

    // 레트로핏 관련
    Call<Dv_option> request;

    // sharedprefernce 가져오기
    SharedPreferences pref ;
    String m_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_preference, container, false);

        pref = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        m_id = pref.getString("m_id", "");

        initWidget(view);

        // 옵션 값 설정
        request = DataService.getInstance().memberAPI.getDvOptionByM_id(m_id);
        request.enqueue(new Callback<Dv_option>() {
            @Override
            public void onResponse(Call<Dv_option> call, Response<Dv_option> response) {
                if (response.code() == 200 && response.body() != null) {
                    Dv_option dvOption = response.body();
                    Log.d("PreferenceFragment", "dvOption : " + dvOption);

                    switch (dvOption.getDo_hurry()) {
                        case "0":
                            fp_dohurry.setChecked(false);
                            break;
                        case "1":
                            fp_dohurry.setChecked(true);
                            break;
                    }

                    switch (dvOption.getDo_navi()) {
                        case "0":
                            fp_donavi.setChecked(false);
                            break;
                        case "1":
                            fp_donavi.setChecked(true);
                            break;
                    }

                    switch (dvOption.getDo_quiet()) {
                        case "0":
                            fp_doquiet.setChecked(false);
                            break;
                        case "1":
                            fp_doquiet.setChecked(true);
                            break;
                    }

                    switch (dvOption.getDo_animal()) {
                        case "0":
                            fp_doanimal.setChecked(false);
                            break;
                        case "1":
                            fp_doanimal.setChecked(true);
                            break;
                    }

                    switch (dvOption.getDo_load()) {
                        case "0":
                            fp_doload.setChecked(false);
                            break;
                        case "1":
                            fp_doload.setChecked(true);
                            break;
                    }

                    switch (dvOption.getDo_child()) {
                        case "0":
                            fp_dochild.setChecked(false);
                            break;
                        case "1":
                            fp_dochild.setChecked(true);
                            break;
                    }

                }
            }

            @Override
            public void onFailure(Call<Dv_option> call, Throwable t) {
                t.printStackTrace();
            }
        });


        updatePreferenceEvent();


        // 뒤로가기
        btn_back_preference = view.findViewById(R.id.btn_back_preference);
        btn_back_preference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                My_info_Fragment my_info_fragment = new My_info_Fragment();
                transaction.replace(R.id.frameLayout, my_info_fragment).commit();
            }
        });

        return view;
    }

    public void initWidget(View view) {
        fp_dohurry = view.findViewById(R.id.fp_dohurry);
        fp_donavi = view.findViewById(R.id.fp_donavi);
        fp_doquiet = view.findViewById(R.id.fp_doquiet);
        fp_doanimal = view.findViewById(R.id.fp_doanimal);
        fp_doload = view.findViewById(R.id.fp_doload);
        fp_dochild = view.findViewById(R.id.fp_dochild);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        request.cancel();
    }

    // 선호운행옵션 이벤트 함수
    public void updatePreferenceEvent() {
        Dv_option dv_option = new Dv_option();

        // 급 가속 , 급운행
        fp_dohurry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dv_option.setM_id(m_id);
                if (isChecked) { // on

                    dv_option.setDo_hurry("1");
                    dv_option.setDo_navi((fp_donavi.isChecked()) ? "1" : "0");
                    dv_option.setDo_quiet((fp_doquiet.isChecked()) ? "1" : "0");
                    dv_option.setDo_animal((fp_doanimal.isChecked()) ? "1" : "0");
                    dv_option.setDo_child((fp_dochild.isChecked()) ? "1" : "0");
                    dv_option.setDo_load((fp_doload.isChecked()) ? "1" : "0");
                    DataService.getInstance().memberAPI.modifyDvOption(dv_option).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 200) {
                                Toast.makeText(getActivity(), "변경 완료", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                        }
                    });


                }else{ //off
                    dv_option.setDo_hurry("0");
                    dv_option.setDo_navi((fp_donavi.isChecked()) ? "1" : "0");
                    dv_option.setDo_quiet((fp_doquiet.isChecked()) ? "1" : "0");
                    dv_option.setDo_animal((fp_doanimal.isChecked()) ? "1" : "0");
                    dv_option.setDo_child((fp_dochild.isChecked()) ? "1" : "0");
                    dv_option.setDo_load((fp_doload.isChecked()) ? "1" : "0");
                    DataService.getInstance().memberAPI.modifyDvOption(dv_option).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 200) {
                                Log.d("과연 성공할까요?", "성공했나요?");
                                Toast.makeText(getActivity(), "변경 완료", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

        // 네비게이션에 따라 이동해 주세요
        fp_donavi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                dv_option.setM_id(m_id);
                if (isChecked) { // on

                    dv_option.setDo_hurry((fp_dohurry.isChecked()) ? "1" : "0");
                    dv_option.setDo_navi("1");
                    dv_option.setDo_quiet((fp_doquiet.isChecked()) ? "1" : "0");
                    dv_option.setDo_animal((fp_doanimal.isChecked()) ? "1" : "0");
                    dv_option.setDo_child((fp_dochild.isChecked()) ? "1" : "0");
                    dv_option.setDo_load((fp_doload.isChecked()) ? "1" : "0");
                    DataService.getInstance().memberAPI.modifyDvOption(dv_option).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 200) {
                                Log.d("과연 성공할까요?", "성공했나요?");
                                Toast.makeText(getActivity(), "변경 완료", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                        }
                    });


                }else{ //off
                    dv_option.setDo_hurry((fp_dohurry.isChecked()) ? "1" : "0");
                    dv_option.setDo_navi("0");
                    dv_option.setDo_quiet((fp_doquiet.isChecked()) ? "1" : "0");
                    dv_option.setDo_animal((fp_doanimal.isChecked()) ? "1" : "0");
                    dv_option.setDo_child((fp_dochild.isChecked()) ? "1" : "0");
                    dv_option.setDo_load((fp_doload.isChecked()) ? "1" : "0");
                    DataService.getInstance().memberAPI.modifyDvOption(dv_option).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 200) {
                                Log.d("과연 성공할까요?", "성공했나요?");
                                Toast.makeText(getActivity(), "변경 완료", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

        // 조용히 가고 싶어요
        fp_doquiet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dv_option.setM_id(m_id);
                if (isChecked) { // on

                    dv_option.setDo_hurry((fp_dohurry.isChecked()) ? "1" : "0");
                    dv_option.setDo_navi((fp_donavi.isChecked()) ? "1" : "0");
                    dv_option.setDo_quiet("1");
                    dv_option.setDo_animal((fp_doanimal.isChecked()) ? "1" : "0");
                    dv_option.setDo_child((fp_dochild.isChecked()) ? "1" : "0");
                    dv_option.setDo_load((fp_doload.isChecked()) ? "1" : "0");
                    DataService.getInstance().memberAPI.modifyDvOption(dv_option).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 200) {
                                Log.d("과연 성공할까요?", "성공했나요?");
                                Toast.makeText(getActivity(), "변경 완료", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                        }
                    });


                }else{ //off
                    dv_option.setDo_hurry((fp_dohurry.isChecked()) ? "1" : "0");
                    dv_option.setDo_navi((fp_donavi.isChecked()) ? "1" : "0");
                    dv_option.setDo_quiet("0");
                    dv_option.setDo_animal((fp_doanimal.isChecked()) ? "1" : "0");
                    dv_option.setDo_child((fp_dochild.isChecked()) ? "1" : "0");
                    dv_option.setDo_load((fp_doload.isChecked()) ? "1" : "0");
                    DataService.getInstance().memberAPI.modifyDvOption(dv_option).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 200) {
                                Log.d("과연 성공할까요?", "성공했나요?");
                                Toast.makeText(getActivity(), "변경 완료", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

        // 반려동물 동반 싫어요
        fp_doanimal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dv_option.setM_id(m_id);
                if (isChecked) { // on

                    dv_option.setDo_hurry((fp_dohurry.isChecked()) ? "1" : "0");
                    dv_option.setDo_navi((fp_donavi.isChecked()) ? "1" : "0");
                    dv_option.setDo_quiet((fp_doquiet.isChecked()) ? "1" : "0");
                    dv_option.setDo_animal("1");
                    dv_option.setDo_child((fp_dochild.isChecked()) ? "1" : "0");
                    dv_option.setDo_load((fp_doload.isChecked()) ? "1" : "0");
                    DataService.getInstance().memberAPI.modifyDvOption(dv_option).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 200) {
                                Log.d("과연 성공할까요?", "성공했나요?");
                                Toast.makeText(getActivity(), "변경 완료", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });


                }else{ //off
                    dv_option.setDo_hurry((fp_dohurry.isChecked()) ? "1" : "0");
                    dv_option.setDo_navi((fp_donavi.isChecked()) ? "1" : "0");
                    dv_option.setDo_quiet((fp_doquiet.isChecked()) ? "1" : "0");
                    dv_option.setDo_animal("0");
                    dv_option.setDo_child((fp_dochild.isChecked()) ? "1" : "0");
                    dv_option.setDo_load((fp_doload.isChecked()) ? "1" : "0");
                    DataService.getInstance().memberAPI.modifyDvOption(dv_option).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 200) {
                                Log.d("과연 성공할까요?", "성공했나요?");
                                Toast.makeText(getActivity(), "변경 완료", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });


                }
            }
        });

        // 유아동반 싫어요
        fp_dochild.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dv_option.setM_id(m_id);
                if (isChecked) { // on

                    dv_option.setDo_hurry((fp_dohurry.isChecked()) ? "1" : "0");
                    dv_option.setDo_navi((fp_donavi.isChecked()) ? "1" : "0");
                    dv_option.setDo_quiet((fp_doquiet.isChecked()) ? "1" : "0");
                    dv_option.setDo_animal((fp_doanimal.isChecked()) ? "1" : "0");
                    dv_option.setDo_child("1");
                    dv_option.setDo_load((fp_doload.isChecked()) ? "1" : "0");
                    DataService.getInstance().memberAPI.modifyDvOption(dv_option).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 200) {
                                Log.d("과연 성공할까요?", "성공했나요?");
                                Toast.makeText(getActivity(), "변경 완료", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });


                }else{ //off
                    dv_option.setDo_hurry((fp_dohurry.isChecked()) ? "1" : "0");
                    dv_option.setDo_navi((fp_donavi.isChecked()) ? "1" : "0");
                    dv_option.setDo_quiet((fp_doquiet.isChecked()) ? "1" : "0");
                    dv_option.setDo_animal((fp_doanimal.isChecked()) ? "1" : "0");
                    dv_option.setDo_child("0");
                    dv_option.setDo_load((fp_doload.isChecked()) ? "1" : "0");
                    DataService.getInstance().memberAPI.modifyDvOption(dv_option).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 200) {
                                Log.d("과연 성공할까요?", "성공했나요?");
                                Toast.makeText(getActivity(), "변경 완료", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });


                }
            }
        });


        // 짐많은 사람 싫어요
        fp_doload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dv_option.setM_id(m_id);
                if (isChecked) { // on

                    dv_option.setDo_hurry((fp_dohurry.isChecked()) ? "1" : "0");
                    dv_option.setDo_navi((fp_donavi.isChecked()) ? "1" : "0");
                    dv_option.setDo_quiet((fp_doquiet.isChecked()) ? "1" : "0");
                    dv_option.setDo_animal((fp_doanimal.isChecked()) ? "1" : "0");
                    dv_option.setDo_child((fp_doanimal.isChecked()) ? "1" : "0");
                    dv_option.setDo_load("1");
                    DataService.getInstance().memberAPI.modifyDvOption(dv_option).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 200) {
                                Log.d("과연 성공할까요?", "성공했나요?");
                                Toast.makeText(getActivity(), "변경 완료", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });


                }else{ //off
                    dv_option.setDo_hurry((fp_dohurry.isChecked()) ? "1" : "0");
                    dv_option.setDo_navi((fp_donavi.isChecked()) ? "1" : "0");
                    dv_option.setDo_quiet((fp_doquiet.isChecked()) ? "1" : "0");
                    dv_option.setDo_animal((fp_doanimal.isChecked()) ? "1" : "0");
                    dv_option.setDo_child((fp_doanimal.isChecked()) ? "1" : "0");
                    dv_option.setDo_load("0");
                    DataService.getInstance().memberAPI.modifyDvOption(dv_option).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 200) {
                                Log.d("과연 성공할까요?", "성공했나요?");
                                Toast.makeText(getActivity(), "변경 완료", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });


                }
            }
        });

    }
}
