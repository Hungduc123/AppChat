import firebaseApp from "../../firebase/config";
import typeMessage from "../../data/typeMessage";
import auth, { FirebaseAuthTypes } from "@react-native-firebase/auth";
export const AddUser = async (
  name: string,
  email: string,
  uid: string,
  profileImg: string
) => {
  let friends: FirebaseAuthTypes.User[] = [];
  try {
    return await firebaseApp
      .database()
      .ref("users/" + uid)
      .set({
        name: name,
        email: email,
        uid: uid,
        profileImg: profileImg,
        friends: friends,
      });
  } catch (e) {
    return e;
  }
};
export const UpdateUser = async (uuid: string, imgSource: string) => {
  try {
    return await firebaseApp
      .database()
      .ref("users/" + uuid)
      .update({
        profileImg: imgSource,
      });
  } catch (error) {
    return error;
  }
};
