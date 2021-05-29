import React, { useState } from "react";
import {
  View,
  Text,
  SafeAreaView,
  TouchableOpacity,
  Alert,
} from "react-native";
import styles from "../styles/styles";
import firebaseApp from "../firebase/config.js";
import { StackNavigationProp } from "@react-navigation/stack";
import { RootStackParamList } from "./RootStackParamList";
import { useNavigation } from "@react-navigation/native";
import { useSelector } from "react-redux";
type HomeScreenProp = StackNavigationProp<RootStackParamList, "Register">;

export default function Setting() {
  const navigation = useNavigation<HomeScreenProp>();

  const [email, setEmail] = useState<string>(
    JSON.stringify(firebaseApp.auth().currentUser)
  );

  return (
    <SafeAreaView style={styles.container}>
      <Text>setting</Text>
      <TouchableOpacity
        onPress={() => {
          Alert.alert("Notification", "Do you want logout", [
            {
              text: "Cancel",
              onPress: () => console.log("Cancel Pressed"),
              style: "cancel",
            },
            {
              text: "OK",
              onPress: () => {
                firebaseApp
                  .auth()
                  .signOut()
                  .then(() => {
                    navigation.navigate("Login");
                    console.log("User signed out!");
                  });
              },
            },
          ]);
        }}
      >
        <Text>Logout</Text>
      </TouchableOpacity>
      <Text>{email}</Text>
    </SafeAreaView>
  );
}
