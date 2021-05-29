import { Dimensions, StyleSheet } from "react-native";
import colors from "../colors/colors";
const { width } = Dimensions.get("window");
const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    padding: 10,
  },
  checkbox: {
    alignSelf: "center",
  },
  input: {
    width: "80%",
    borderRadius: 20,
    borderColor: colors.first,
    borderWidth: 1,
  },

  text: {
    fontSize: 30,
  },
  name: {
    fontWeight: "bold",
    fontSize: 17,
  },
  image: {
    width: 100,
    height: 100,
    backgroundColor: "black",
    borderRadius: 20,
    borderColor: colors.first,
    borderWidth: 2,
  },
  child: { width, justifyContent: "center" },
  card: {
    width: "80%",
    borderRadius: 20,
    borderColor: colors.first,
    borderWidth: 1,
    justifyContent: "center",
    alignItems: "center",
  },
});
export default styles;
