import auth from "@react-native-firebase/auth";
const registerAuth = (userName: string, password: string) => {
  auth()
    .createUserWithEmailAndPassword(userName, password)
    .then(() => {
      console.log(
        "User account created & signed in!" + userName + ": " + password
      );
    })
    .catch((error) => {
      if (error.code === "auth/email-already-in-use") {
        console.log("That email address is already in use!");
      }

      if (error.code === "auth/invalid-email") {
        console.log("That email address is invalid!");
      }

      console.error(error);
    });
};
export default registerAuth;
