import firebase from "../../firebase/config.js";
const SignUpRequest = async (email, password) => {
  try {
    return await firebase
      .auth()
      .createUserWithEmailAndPassword(email, password);
  } catch (e) {
    return e;
  }
};
export default SignUpRequest;
