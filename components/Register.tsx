import { Card } from "native-base";
import React, { useState } from "react";
import {
  KeyboardAvoidingView,
  SafeAreaView,
  Text,
  View,
  Image,
  TextInput,
  TouchableOpacity,
  Platform,
  Alert,
  Modal,
} from "react-native";
import { useHistory } from "react-router-dom";
import colors from "../colors/colors";
import registerAuth from "../firebase/registerAuth";
import styles from "../styles/styles";
// import auth from "@react-native-firebase/auth";
import SignUpRequest from "../network/Register";
import firebase from "firebase";
// import AddUser from "../network/User";
import firebaseApp from "../firebase/config.js";
import Loading from "./Loading";
import { keys, setAsyncStorage } from "../asyncStorage";
import { useNavigation } from "@react-navigation/native";
import { StackNavigationProp } from "@react-navigation/stack";
import { RootStackParamList } from "./RootStackParamList";
import { AddUser } from "../network/User";
type RegisterScreenProp = StackNavigationProp<RootStackParamList, "Register">;

function Register() {
  const history = useHistory();
  const navigation = useNavigation<RegisterScreenProp>();
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [fullName, setFullName] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(false);

  return (
    <SafeAreaView style={styles.container}>
      <KeyboardAvoidingView
        behavior={Platform.OS === "ios" ? "padding" : "height"}
        style={styles.container}
      >
        <View
          style={{
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          <Text
            style={{
              fontSize: 30,
              fontWeight: "bold",
              color: "#CC6666",
              paddingBottom: 10,
            }}
          >
            Register
          </Text>
        </View>
        <Card
          style={{
            borderRadius: 20,
            width: "90%",
            padding: 10,
          }}
        >
          <Card
            style={{
              flexDirection: "row",
              justifyContent: "space-between",
              alignItems: "center",
              borderRadius: 20,
              shadowColor: colors.first,
            }}
          >
            <Text style={{ color: colors.first }}>Full Name: </Text>
            <TextInput
              textAlign="center"
              onChangeText={(Value) => setFullName(Value)}
              style={{
                height: 40,
                width: 200,
                borderColor: colors.first,

                borderWidth: 1,
                // backgroundColor: "#ADDFFF",

                borderRadius: 30,
              }}
              placeholderTextColor="gray"
              placeholder="Enter your full name"
              value={fullName}
            />
          </Card>
          <Card
            style={{
              flexDirection: "row",
              justifyContent: "space-between",
              alignItems: "center",
              borderRadius: 20,
              shadowColor: colors.first,
            }}
          >
            <Text style={{ color: colors.first }}>Email: </Text>
            <TextInput
              textAlign="center"
              onChangeText={(Value) => setEmail(Value)}
              style={{
                height: 40,
                width: 200,
                borderColor: colors.first,

                borderWidth: 1,
                // backgroundColor: "#ADDFFF",

                borderRadius: 30,
              }}
              placeholderTextColor="gray"
              placeholder="Enter your email"
              value={email}
            />
          </Card>
          <Card
            style={{
              flexDirection: "row",
              justifyContent: "space-between",
              alignItems: "center",
              borderRadius: 20,
              shadowColor: colors.first,
            }}
          >
            <Text style={{ color: colors.first }}>Password:</Text>
            <TextInput
              textAlign="center"
              onChangeText={(Value) => setPassword(Value)}
              style={{
                height: 40,
                width: 200,
                borderColor: colors.first,
                borderWidth: 1,
                borderRadius: 30,
                justifyContent: "center",
                alignItems: "center",
              }}
              placeholderTextColor="gray"
              placeholder="Enter your password"
              secureTextEntry
              value={password}
            />
          </Card>
        </Card>
        <View
          style={{
            flexDirection: "row",
            paddingTop: 30,
          }}
        >
          <TouchableOpacity
            // onPress={() => {
            //   // history.goBack();
            // }}
            onPress={() => navigation.navigate("Login")}
            style={{
              backgroundColor: "orange",

              borderRadius: 20,
              width: 100,
              height: 50,
              justifyContent: "center",
              alignItems: "center",
            }}
          >
            <Text>Login</Text>
          </TouchableOpacity>
          <TouchableOpacity
            onPress={() => {
              // alert("aaa");

              setLoading(true);
              firebaseApp
                .auth()
                .createUserWithEmailAndPassword(email, password)
                .then(() => {
                  console.log("User account created & signed in!");
                  let uid = firebaseApp.auth().currentUser.uid;
                  let profileImg =
                    "https://e7.pngegg.com/pngimages/505/761/png-clipart-login-computer-icons-avatar-icon-monochrome-black-thumbnail.png";
                  AddUser(fullName, email, uid, profileImg).then(() => {
                    // setAsyncStorage(keys.uuid, uid);
                    // setUniqueValue(uid);
                    setLoading(false);
                  });
                })
                .catch((error: any) => {
                  if (error.code === "auth/email-already-in-use") {
                    console.log("That email address is already in use!");
                    setLoading(false);
                    alert("That email address is already in use!");
                  }

                  if (error.code === "auth/invalid-email") {
                    console.log("That email address is invalid!");
                    setLoading(false);
                    alert("That email address is invalid!");
                  }

                  console.error(error);
                });
            }}
            style={{
              backgroundColor: "orange",
              borderRadius: 20,
              width: 100,
              height: 50,
              justifyContent: "center",
              alignItems: "center",
            }}
          >
            <Text>Register</Text>
          </TouchableOpacity>
        </View>
        {loading && (
          <Modal animationType="fade" transparent={true} visible={loading}>
            <Loading></Loading>
          </Modal>
        )}
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

export default Register;
