package cl.usm.telematica.sigamobile;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CredentialsActivity extends AppCompatActivity {
    private List<User> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private AppCompatButton btnDeleteSelected;
    private ViewGroup layout;
    private UserTable db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);
        layout = (ViewGroup) findViewById(R.id.activity_results);
        btnDeleteSelected = (AppCompatButton) findViewById(R.id.delete_button);
        db = new UserTable(getApplicationContext());
        userList = db.getAllUsers();

        if (!userList.isEmpty()){
            try {
                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                    userAdapter = new UserAdapter(userList);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager repoLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(repoLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(userAdapter);


                    userAdapter.notifyDataSetChanged();
                    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            User user1 = userList.get(position);

                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));


            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            TextView error_url = (TextView) findViewById(R.id.textView);
            error_url.setText("No hay usuarios aún");
            error_url.setGravity(View.TEXT_ALIGNMENT_CENTER);

        }

        btnDeleteSelected.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (!userList.isEmpty()){
                    for (User e : userList){
                        if (e.isSelected()){
                            db.deleteUser(e);
                            userList.remove(e);
                        }
                    }
                    userAdapter.notifyDataSetChanged();
                    Toast.makeText(CredentialsActivity.this, "Usuarios Eliminados", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
