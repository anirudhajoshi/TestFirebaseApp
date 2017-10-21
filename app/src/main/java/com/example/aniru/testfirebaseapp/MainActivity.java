package com.example.aniru.testfirebaseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 1;
    private BookDetails_FB bookDetail;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mBookDetailDatabaseReference;
    private ValueEventListener mBooksListener;
    private ChildEventListener mChildEventListener;

    private ListView mMessageListView;
    private BooksAdapter mBooksAdapter;
    
    List<BookDetails_FB> mBooks = new ArrayList<BookDetails_FB>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMessageListView = findViewById(R.id.lvBookDetails);
        mBooksAdapter = new BooksAdapter(this, R.layout.item_bookdetails, mBooks);
        mMessageListView.setAdapter(mBooksAdapter);

        SetupTestData();

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(
                                    Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()) )
                            .build(),
                    RC_SIGN_IN);

        } else {
            Toast.makeText(this, "Already signed in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if( mBookDetailDatabaseReference!=null&& mBooksListener!=null )
            mBookDetailDatabaseReference.removeEventListener(mBooksListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode==RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in", Toast.LENGTH_SHORT).show();
                try {
                    Toast.makeText(this, " Signed in", Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    Log.d("BookDetailsActivity", e.toString());
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void savetoFB(View view) {

        // int i = 0;
        // writeNewBookDetail(i);

        for( int i=0;i< mBooks.size(); i++){
            Log.d("MainActivity",mBooks.get(i).getTitle());
            writeNewBookDetail(i);
        }
    }

    // private void writeNewBookDetail(BookDetails_FB bookDetails) {
    private void writeNewBookDetail(int i) {
        try {

            mDatabase.child("books").push().setValue(mBooks.get(i));

            // mDatabase.child("books").child(isbn).setValue(bookDetails);
/*            String fbKey = mDatabase.child("books").push().getKey();
            mDatabase.child("books").child(fbKey).setValue(mBooks.get(i));*/
            //mBooks.get(i).setFbKey(fbKey);
            //Log.d("MainActivity", mBooks.get(i).getFbKey());
        }
        catch (Exception e){
            Log.d("MainActivity",e.toString());
        }
    }

    public void UpdateFBNode(View view) {

        // updateExistingBookDetail_Title("1","A New Title will go here");
        int i=0;
        updateExistingBookDetail_Title(i,"A New Title will go here");

    }

    // private void updateExistingBookDetail_Title(String isbn,String newTitle) {
    private void updateExistingBookDetail_Title(int i,String newTitle) {

        try {
            // mDatabase.child("books").child(isbn).child("title").setValue(newTitle);
            //mDatabase.child("books").child(mBooks.get(i).getFbKey()).child("title").setValue(newTitle);
        }
        catch (Exception e){
            Log.d("MainActivity",e.toString());
        }
    }

    public void ReadNode(View view) {
        if( mChildEventListener == null) {
            mDatabase.child("books").addChildEventListener(mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    BookDetails_FB bookDetails = dataSnapshot.getValue(BookDetails_FB.class);
                    mBooksAdapter.add(bookDetails);
                    Log.d("MainActivity", bookDetails.getTitle());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
/*
        if( mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    BookDetails_FB bookDetails = dataSnapshot.getValue(BookDetails_FB.class);
                    *//*for (DataSnapshot booksSnapshot: dataSnapshot.getChildren()) {
                        BookDetails_FB bookDetails = dataSnapshot.getValue(BookDetails_FB.class);
                        Log.d("MainActivity",bookDetails.getTitle());
                    }*//*
                    Log.d("MainActivity",bookDetails.getTitle());
                    // mMessageAdapter.add(friendlyMessage);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mDatabase.addChildEventListener(mChildEventListener);
        }*/
        /*
        Query myQuery = mDatabase.child("books");
        myQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("MainActivity","OnChildAdded");
                for (DataSnapshot booksSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle it
                    BookDetails_FB bk = booksSnapshot.getValue(BookDetails_FB.class);
                    // String title = booksSnapshot.getValue(BookDetails_FB.class).getTitle();
                    // String isbn = booksSnapshot.getValue(BookDetails_FB.class).getIsbn();
                    // List<String> authors = booksSnapshot.getValue(BookDetails_FB.class).getAuthors();
                    // Log.e("MainActivity", "Title: " +  title);
                    // Log.e("MainActivity", "ISBN: " +  isbn);
                    // Log.e("MainActivity", "Authors: " +  authors.toString());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("MainActivity","OnChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("MainActivity","OnChildRemoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("MainActivity","OnChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MainActivity","OnCancelled");
            }
        });
        */

        /*
        int i = 0;

        mBookDetailDatabaseReference = mDatabase.getDatabase()
                                        .getReference()
                                        .child("books")
                                        .child(mBooks.get(i).getFbKey());

        mBooksListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                BookDetails_FB bookDetails = dataSnapshot.getValue(BookDetails_FB.class);
                Toast.makeText(MainActivity.this, bookDetails.getTitle(), Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", bookDetails.getTitle());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("MainActivity", "loadPost:onCancelled", databaseError.toException());
            }
        };

        mBookDetailDatabaseReference.addListenerForSingleValueEvent(mBooksListener);
        */
    }

    public void Signout(View view) {

        try{
            AuthUI.getInstance().signOut(this);
        }
        catch(Exception e){
            Log.d("MainActivity",e.toString());
        }
    }

    public void DeleteNode(View view) {

        try{
            deleteSpecifiedBookDetail("1");
        }
        catch(Exception e){
            Log.d("MainActivity",e.toString());
        }
    }


    private void deleteSpecifiedBookDetail(String isbn) {

        try {
            // mDatabase.child("books").child(isbn).removeValue();
            // mDatabase.child("books").child(isbn).setValue(null);
            mDatabase.child("books").orderByChild("isbn").equalTo("5").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // dataSnapshot.getRef().setValue(null);
                            dataSnapshot.getRef().removeValue();
                            // Log.d("MainActivity",dataSnapshot.getValue().toString());
                            // dataSnapshot.getRef().removeValue();
                            // String key = dataSnapshot.getKey();
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                        }
                    });

        }
        catch (Exception e){
            Log.d("MainActivity",e.toString());
        }
    }

    public void CheckAndSave(View view) {

        try {
            String isbn = "1";
            Query myTopPostsQuery = mDatabase.child("books").orderByChild("isbn").equalTo("1");
            myTopPostsQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    BookDetails_FB bd = dataSnapshot.getValue(BookDetails_FB.class);

                    Log.d("MainActivity",bd.getTitle());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } catch (Exception e) {
            Log.d("MainActivity", e.toString());
        }
    }

    private void SetupTestData() {
        List<String> authors = new ArrayList<String>();

        BookDetails_FB bookDetail1 = new BookDetails_FB();
        bookDetail1.setTitle("Ivanhoe");
        authors.add("Sir Walter Scott");
        // bookDetail1.setAuthors(authors);
        authors.clear();
        bookDetail1.setIsbn("1");
        bookDetail1.setPageCount(100);
        mBooks.add(bookDetail1);

        BookDetails_FB bookDetail2 = new BookDetails_FB();
        bookDetail2.setTitle("The Murder of Roger Akroyd");
        authors.add("Agatha Christie");
        // bookDetail2.setAuthors(authors);
        authors.clear();
        bookDetail2.setIsbn("2");
        bookDetail2.setPageCount(200);
        mBooks.add(bookDetail2);

        BookDetails_FB bookDetail3 = new BookDetails_FB();
        bookDetail3.setTitle("C");
        authors.add("Kernighan");
        authors.add("Ritchie");
        // bookDetail3.setAuthors(authors);
        authors.clear();
        bookDetail3.setIsbn("3");
        bookDetail3.setPageCount(300);
        mBooks.add(bookDetail3);

        BookDetails_FB bookDetail4 = new BookDetails_FB();
        bookDetail4.setTitle("M");
        authors.add("Egon Jacobson");
        authors.add("Fritz Lang");
        authors.add("Thea von Harbou");
        // bookDetail4.setAuthors(authors);
        authors.clear();
        bookDetail4.setIsbn("4");
        bookDetail4.setPageCount(400);
        mBooks.add(bookDetail4);

        BookDetails_FB bookDetail5 = new BookDetails_FB();
        bookDetail5.setTitle("Mein Kampf");
        authors.add("Adolf Hitler");
        // bookDetail5.setAuthors(authors);
        authors.clear();
        bookDetail5.setIsbn("5");
        bookDetail5.setPageCount(500);
        mBooks.add(bookDetail5);

        BookDetails_FB bookDetail6 = new BookDetails_FB();
        bookDetail6.setTitle("In the shadow of freedom");
        authors.add("Laxmi Dhaul");
        // bookDetail6.setAuthors(authors);
        authors.clear();
        bookDetail6.setIsbn("6");
        bookDetail6.setPageCount(1000);
        mBooks.add(bookDetail6);
    }
}
