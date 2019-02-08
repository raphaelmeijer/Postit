package rma.postit.helper;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import rma.postit.model.Category;
import rma.postit.model.Post;

public class FirebaseConnector {
    private static FirebaseConnector unique = new FirebaseConnector();

    public static FirebaseConnector getInstance(){
        return unique;
    }

    // FIREBASE INSTANCES
    private FirebaseFirestore getFirestore(){
        return FirebaseFirestore.getInstance();
    }

    private FirebaseAuth getFireauth(){
        return FirebaseAuth.getInstance();
    }

    public String getCurrentUserId(){
        return getFireauth().getUid();
    }

    public CollectionReference getCategoryPosts(String categoryId){
        return getUserCategory(categoryId)
                .collection("posts");
    }

    public CollectionReference getUserCategories(){
        return getFirestore()
                .collection("users")
                .document(getCurrentUserId())
                .collection("categories");
    }

    public DocumentReference getUserCategory(String id){
        return getUserCategories().document(id);
    }

    public DocumentReference getUserPostByCategoryAndId(String categoryId, String postId){
        return getUserCategory(categoryId)
                .collection("posts")
                .document(postId);
    }
    public DocumentReference createUserPostByCategory(String categoryId, String postId){
        return getUserCategory(categoryId)
                .collection("posts")
                .document(postId);
    }
    public boolean isUserLoggedIn(){
        return getFireauth().getCurrentUser() != null;
    }

    public UserInfo getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public Task<AuthResult> signInWithEmail(String username, String password) {
        return getFireauth().signInWithEmailAndPassword(username, password);
    }

    public Task<AuthResult> signUpWithEmail(String email, String password) {
        return getFireauth().createUserWithEmailAndPassword(email, password);
    }

    public void createBaseUserInfo() {
        setStandardCategories();
    }

    private void setStandardCategories() {
        for( String name : Globals.CATEGORIES ){
            Category category = new Category();
            category.setName(name);
            getUserCategories().document(category.getId()).set(category, SetOptions.merge());
        }
    }
}
