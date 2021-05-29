import firebaseApp from "./firebase/config.js";
export default userCurrent = firebaseApp.auth().currentUser;
