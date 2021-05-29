import { Card } from "native-base";
import React, { useEffect, useState } from "react";
import {
  View,
  Text,
  SafeAreaView,
  FlatList,
  TouchableOpacity,
  Image,
} from "react-native";
import colors from "../colors/colors";
import styles from "../styles/styles";
import firebaseApp from "../firebase/config.js";
// import auth, { FirebaseAuthTypes } from "@react-native-firebase/auth";
import { log } from "react-native-reanimated";
import dataUser from "../data/dataUser";
import ShowUsers from "./showUser";
import { Avatar } from "react-native-elements";
import { UpdateUser } from "../network/User";
import * as ImagePicker from "expo-image-picker";
import { chooseItem } from "../slice/chooseItem";
import { useDispatch, useSelector } from "react-redux";
import { StackNavigationProp } from "@react-navigation/stack";
import { RootStackParamList } from "./RootStackParamList";
import { useNavigation } from "@react-navigation/native";
type ListFriendScreenProp = StackNavigationProp<
  RootStackParamList,
  "ListFriend"
>;

export default function ListFriend() {
  // let dataUserCurrent: FirebaseAuthTypes.User;
  const navigation = useNavigation<ListFriendScreenProp>();
  const user = firebaseApp.auth().currentUser;
  const dispatch = useDispatch();
  const [userDetail, setUserDetail] = useState<dataUser>({
    email: "",
    name: "",
    profileImg: "",
    uid: "",
  });
  const [allUsers, setAllUsers] = useState<dataUser[]>([]);

  const [dataUserCurrent, setDataUserCurrent] = useState<dataUser>();

  useEffect(() => {
    try {
      firebaseApp
        .database()
        .ref("/users")
        .on("value", (snapshot: any) => {
          let users: dataUser[] = [];
          let currentUser = {
            email: "",
            message: [],
            name: "",
            profileImg: "",
            uid: "",
          };
          snapshot.forEach((child: any) => {
            if (user.uid === child.val().uid) {
              currentUser.email = child.val().email;
              currentUser.message = child.val().message;
              currentUser.name = child.val().name;
              currentUser.profileImg = child.val().profileImg;
              currentUser.uid = child.val().uid;
            } else {
              users.push({
                email: child.val().email,

                name: child.val().name,
                profileImg: child.val().profileImg,
                uid: child.val().uid,
              });
            }
          });

          setUserDetail(currentUser);
          setAllUsers(users);
        });
    } catch (error) {
      console.log(error);
    }

    // Stop listening for updates when no longer required
  }, []);
  async function pickImage(uid: string) {
    let result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.All,
      allowsEditing: true,
      aspect: [1, 1],
      quality: 1,
    });

    console.log(result);

    if (!result.cancelled) {
      setUserDetail({
        ...userDetail,
        profileImg: result.uri,
      });

      UpdateUser(uid, result.uri)
        .then(() => {
          // setUserDetail({
          //   ...userDetail,
          //   profileImg: result.uri,
          // });
        })
        .catch((err: any) => {
          alert(err);
        });
    }
  }
  // const changeAvatar = (uid: string) => {

  //   ImagePicker.showImagePicker(options, (response: any) => {
  //     console.log("Response = ", response);

  //       // Base 64 image:
  //       let source = "data:image/jpeg;base64," + response.data;

  //       UpdateUser(uid, source)
  //         .then(() => {
  //           setUserDetail({
  //             ...userDetail,
  //             profileImg: source,
  //           });
  //         })
  //         .catch((err: any) => {
  //           alert(err);
  //         });

  // };
  const RederList = (props: any) => {
    return (
      <TouchableOpacity
        onPress={() => {
          const action = chooseItem(props.it);
          dispatch(action);
          navigation.navigate("Chat");
        }}
      >
        <Avatar
          rounded
          source={{
            uri: props.it.profileImg,
          }}
        />

        <Text>{props.it.name}</Text>
      </TouchableOpacity>
    );
  };
  return (
    <SafeAreaView style={styles.container}>
      <Card style={styles.card}>
        <TouchableOpacity
          onPress={() => {
            pickImage(userDetail.uid!);
          }}
        >
          <Avatar
            size="large"
            rounded
            source={{
              uri: userDetail.profileImg,
            }}
          />
        </TouchableOpacity>

        <Text>{userDetail.name}</Text>
      </Card>
      <FlatList
        style={{ flex: 1 }}
        data={allUsers}
        keyExtractor={(item) => item.uid!.toString()}
        renderItem={({ item }) => <RederList it={item}></RederList>}
      ></FlatList>
    </SafeAreaView>
  );
}
