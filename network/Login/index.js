import firebase from "../../firebase/config.js";
const loginRequest = async (email, password) => {
  try {
    return await firebase.auth().signInWithEmailAndPassword(email, password);
  } catch (e) {
    return e;
  }
};
export default loginRequest;
