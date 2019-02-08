package rma.postit.helper;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirestorageHelper {
    private static FirestorageHelper unique = new FirestorageHelper();

    public static FirestorageHelper getInstance(){ return unique; }

    private FirebaseStorage getFireStorage(){
        return FirebaseStorage.getInstance();
    }

    /**
     * Will try to upload the image.
     *
     * @param fileName
     * @param profileImageBytes
     * @return Will return the upload task generated.
     */
    public UploadTask uploadImageToStorage(String fileName, byte[] profileImageBytes) {
        StorageReference reference = getStorageReference(fileName);
        // return the upload task
        return reference.putBytes(profileImageBytes);
    }

    /**
     * Get the given reference but with full filename
     * @param fileName
     * @return
     */
    public StorageReference getStorageReference(String fileName) {
        // get reference
        return getFireStorage().getReference().child("pictures").child(fileName);
    }
}
