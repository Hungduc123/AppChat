import firebaseApp from "../../firebase/config";

export const senderMsg = async (
  msgValue: string,
  currentUserId: string,
  guestUserId: string,
  img: string,
  time: string
) => {
  try {
    return await firebaseApp
      .database()
      .ref("messages/" + currentUserId)
      .child(guestUserId)
      .push({
        messege: {
          sender: currentUserId,
          reciever: guestUserId,
          msg: msgValue,
          img: img,
          time: time,
        },
      });
  } catch (error) {
    return error;
  }
};

export const recieverMsg = async (
  msgValue: string,
  currentUserId: string,
  guestUserId: string,
  img: string,
  time: string
) => {
  try {
    return await firebaseApp
      .database()
      .ref("messages/" + guestUserId)
      .child(currentUserId)
      .push({
        messege: {
          sender: currentUserId,
          reciever: guestUserId,
          msg: msgValue,
          img: img,
          time: time,
        },
      });
  } catch (error) {
    return error;
  }
};
