import React from "react";
import { View, Text } from "react-native";
import styles from "../styles/styles";

export default function Loading() {
  return (
    <View style={styles.container}>
      <Text>loading...</Text>
    </View>
  );
}
