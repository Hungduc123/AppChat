import React from "react";
import { View, Text, SafeAreaView } from "react-native";
import userCurrent from "../network/userCurrent";
import styles from "../styles/styles";
import firebaseApp from "../firebase/config.js";
import { useEffect } from "react";
import { CurrentUser } from "../slice/CurrentUser";
import { useDispatch } from "react-redux";

export default function Home() {
  return (
    <SafeAreaView style={styles.container}>
      <Text>Home {firebaseApp.auth().currentUser.email}</Text>
    </SafeAreaView>
  );
}
