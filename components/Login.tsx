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
  Modal,
} from "react-native";
import { useHistory } from "react-router-dom";
import colors from "../colors/colors";
import styles from "../styles/styles";
import firebaseApp from "../firebase/config.js";
import Loading from "./Loading";
import { StackNavigationProp } from "@react-navigation/stack";
import { RootStackParamList } from "./RootStackParamList";
import { useNavigation } from "@react-navigation/native";
import { CurrentUser } from "../slice/CurrentUser";
import { useDispatch } from "react-redux";
import { log } from "react-native-reanimated";
type authScreenProp = StackNavigationProp<RootStackParamList, "Login">;

function Login() {
  const navigation = useNavigation<authScreenProp>();
  const history = useHistory();
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(false);
  const dispatch = useDispatch();

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
            Welcome
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
            <Text style={{ color: colors.first }}>User Name: </Text>
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
              placeholder="Enter your user name"
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
            onPress={() => {
              setLoading(true);
              firebaseApp
                .auth()
                .signInWithEmailAndPassword(email, password)
                .then((userCredential: any) => {
                  var user = userCredential.user;
                  console.log("login complete");
                  setLoading(false);
                  // history.push("/Home");
                  setEmail("");
                  setPassword("");

                  navigation.navigate("Home");

                  alert("login complete");
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
                  setLoading(false);
                  alert("wrong");
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
            <Text>Login</Text>
          </TouchableOpacity>
          <TouchableOpacity
            // history.push("/Register");
            onPress={() => {
              navigation.navigate("Register");
              setEmail("");
              setPassword("");
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

export default Login;
