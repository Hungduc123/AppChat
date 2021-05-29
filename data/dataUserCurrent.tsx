// import auth, { FirebaseAuthTypes } from "@react-native-firebase/auth";
// import { useEffect } from "react";
// import firebaseApp from "../firebase/config.js";

//  let dataUserCurrent: FirebaseAuthTypes.User;
//  function User(userId: any):FirebaseAuthTypes.User {

//   useEffect(() => {
//     const onValueChange = firebaseApp
//       .database()
//       .ref(`/users/${userId}`)
//       .on("value", (snapshot: any) => {
//         dataUserCurrent = snapshot.val();
//         console.log("User data: ", dataUserCurrent);
//       });

//     // Stop listening for updates when no longer required
//     return () =>
//       firebaseApp
//         .database()
//         .ref(`/users/${userId}`)
//         .off("value", onValueChange);
//   }, [userId]);
//   return dataUserCurrent
// }
// export default  User
