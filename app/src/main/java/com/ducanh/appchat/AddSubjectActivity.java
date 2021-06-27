package com.ducanh.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ducanh.appchat.activity.ClassActivity;
import com.ducanh.appchat.api.DetectImage;
import com.ducanh.appchat.model.Question;
import com.ducanh.appchat.model.Subject;
import com.ducanh.appchat.model.Test;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;
import com.isseiaoki.simplecropview.CropImageView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.ducanh.appchat.TranslateActivity.REQUEST_IMAGE_CAPTURE;

public class AddSubjectActivity extends AppCompatActivity {
    private EditText txtSubjectname,txtQuestion,txtAnswerA,txtAnswerB,txtAnswerC;
    ImageButton imageQuestion;
    TextView txtSubjectName2,txtTestName2,txtAnswer;
    private Button btnAddTest,btnAddSubject,btnBack;
    Spinner subjectSpinner,testSpinner,spinnerAnswer;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Bitmap imageBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String className="";
    private final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        txtSubjectname=findViewById(R.id.edit_subjectName);
        txtQuestion=findViewById(R.id.edit_question);
        txtAnswer=findViewById(R.id.txt_answer);

        spinnerAnswer=findViewById(R.id.answer_spinner);
        txtAnswerA=findViewById(R.id.edit_answerA);
        txtAnswerB=findViewById(R.id.edit_answerB);
        txtAnswerC=findViewById(R.id.edit_answerC);

        subjectSpinner=findViewById(R.id.subject_spinner);
        testSpinner=findViewById(R.id.test_spinner);

        txtTestName2=findViewById(R.id.txt_testName);
        txtSubjectName2=findViewById(R.id.txt_subjectName);

        btnAddTest=findViewById(R.id.btn_addTest);
        btnAddSubject=findViewById(R.id.btn_addSubject);
        btnBack=findViewById(R.id.btn_back);
        imageQuestion=findViewById(R.id.btn_image_question);


        Intent intent=new Intent();
        intent=getIntent();
        className=intent.getStringExtra("className");

        setSpinner();

        imageQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    chooseImage();
//                    .setAspectRatio(1,1)
                CropImage.activity()
                        .start(AddSubjectActivity.this);

//                dispatchTakePictureIntent();
            }
        });

//        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
//        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());


        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectName=txtSubjectname.getText().toString();
                Subject subject=new Subject(subjectName,"0");
                addSubject(subject);
                Toast.makeText(AddSubjectActivity.this, "Thêm môn học thành công",Toast.LENGTH_SHORT).show();
            }
        });

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Adapter adapter = parent.getAdapter();
                txtSubjectName2.setText(adapter.getItem(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        testSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Adapter adapter = parent.getAdapter();
                txtTestName2.setText(adapter.getItem(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerAnswer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Adapter adapter = parent.getAdapter();
                txtAnswer.setText(adapter.getItem(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAddTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question=txtQuestion.getText().toString();
                String answer=txtAnswer.getText().toString();
                String subjectName=txtSubjectName2.getText().toString();
                String testName=txtTestName2.getText().toString();
                String answerA=txtAnswerA.getText().toString();
                String answerB=txtAnswerB.getText().toString();
                String answerC=txtAnswerC.getText().toString();


                if (TextUtils.isEmpty((subjectName)) || TextUtils.isEmpty(question)
                        || TextUtils.isEmpty(answer)){
                    Toast.makeText(AddSubjectActivity.this, "Hãy nhập tất cả các dòng",Toast.LENGTH_SHORT).show();

                }else{
                    Question question1=new Question(question,answerA,answerB,answerC,answer);
                    Test test=new Test(testName,subjectName,question1);
                    addTest(test);
                    Toast.makeText(AddSubjectActivity.this, "Thêm đề thành công",Toast.LENGTH_SHORT).show();

                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (className!=null){
                    submitFeed(txtSubjectName2.getText().toString(),"subject");
                    Intent intent=new Intent(AddSubjectActivity.this, ClassActivity.class);
                    intent.putExtra("class",className);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(AddSubjectActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });




    }
    public void setSpinner(){
        getSubject();
        String tests[]={"15 Phút","Giữ kỳ","Học kỳ"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                tests);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.testSpinner.setAdapter(adapter2);

        //set spinner answer

        String answer[]={"A","B","C"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                answer);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerAnswer.setAdapter(adapter3);
    }

    private void getSubject(){
//        List<Subject> subjects=new ArrayList<>();
        List<String> subjectName=new ArrayList<>();
//        Query reference=FirebaseDatabase.getInstance().getReference("Subject").orderByChild("question").equalTo("ggggg");
        reference=FirebaseDatabase.getInstance().getReference("Subjects");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectName.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Subject subject=snapshot1.getValue(Subject.class);
//                    subjects.add(subject);
                    subjectName.add(subject.getName());

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddSubjectActivity.this,
                        android.R.layout.simple_spinner_item,
                        subjectName);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subjectSpinner.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private List<Test> getTest(){
        List<Test> listTest=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Tests");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTest.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Test test=snapshot1.getValue(Test.class);
                    listTest.add(test);
                    System.out.println(test +"   "+ test.getQuestion().getQuestion());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listTest;
    }
    private void addSubject(Subject subject) {
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("name",subject.getName());
        hashMap.put("point",subject.getPoint());
        FirebaseDatabase.getInstance().getReference().child("Subjects").push().setValue(hashMap);
    }
    private void addTest(Test test) {

        HashMap<String,Object> hashMap2=new HashMap<>();
        hashMap2.put("name",test.getName());
        hashMap2.put("subjectName",test.getSubjectName());
        hashMap2.put("question",test.getQuestion());
        FirebaseDatabase.getInstance().getReference().child("Tests").push().setValue(hashMap2);
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            imageBitmap = (Bitmap) extras.get("data");
////            imageView.setImageBitmap(imageBitmap);
//            detect();
//        }


        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            Uri imageUri=result.getUri();
//            Uri imageUri=data.getData();
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("chay qua day======================");
            detect();
        }
    }
    private void detect() {
        System.out.println("co chay qua day---------");
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);

        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getCloudTextRecognizer();
        FirebaseVisionCloudTextRecognizerOptions options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
                .setLanguageHints(Arrays.asList("en", "hi"))
                .build();
        Task<FirebaseVisionText> result =
                detector.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                displayText(firebaseVisionText);
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
    }
    private void displayText(FirebaseVisionText result) {
//        String resultText = result.getText();
//        String text="";
//        for (FirebaseVisionText.TextBlock block: result.getTextBlocks()) {
//            String blockText = block.getText();
//            text+=" "+blockText;
//        }

        String question="";
        String answerA="";
        String answerB="";
        String answerC="";

        for (FirebaseVisionText.TextBlock block: result.getTextBlocks()) {
            String blockText = block.getText();

            for (FirebaseVisionText.Line line: block.getLines()) {
                String lineText = line.getText();
                System.out.println("Line============="+lineText);

                for (FirebaseVisionText.Element element: line.getElements()) {
                    String elementText = element.getText();

                    if (elementText.equalsIgnoreCase("Question") || elementText.equalsIgnoreCase("Cau") ||
                            elementText.equalsIgnoreCase("Câu")){
                        question=lineText;
                    }
                    if (elementText.equals("A.")){
                        answerA=lineText;
                    }
                    if (elementText.equals("B.")){
                        answerB=lineText;
                    }
                    if (elementText.equals("C.")){
                        answerC=lineText;
                    }

                }
            }
        }
        answerA=answerA.replace("A. ","");
        answerB=answerB.replace("B. ","");
        answerC=answerC.replace("C. ","");

        String[] questions=question.split(" ");
        String question2="";
        for (int i=2;i<questions.length;i++)
            question2+=questions[i]+" ";

        System.out.println(question2+"  "+answerA+"  "+answerB+"  "+answerC+"++++++++++++++");

        txtQuestion.setText("");
        txtQuestion.setText(question2);

        txtAnswerA.setText("");
        txtAnswerA.setText(answerA);

        txtAnswerB.setText("");
        txtAnswerB.setText(answerB);

        txtAnswerC.setText("");
        txtAnswerC.setText(answerC);

    }
    private void submitFeed(String content,String type){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference classRef= FirebaseDatabase.getInstance().getReference("Class")
                .child(className);

        Calendar c = Calendar.getInstance();
        int hour, minute, second,day,month;
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        day=c.get(Calendar.DAY_OF_MONTH);
        month=c.get(Calendar.MONTH);
        String date=minute+"/"+hour+"/"+day+"/"+month;

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("userID",firebaseUser.getUid());
        hashMap.put("content",content);
        hashMap.put("type",type);
        hashMap.put("date",date);

        classRef.push().setValue(hashMap);

    }



}