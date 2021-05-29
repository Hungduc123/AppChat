export const keys = {
  uuid: "uuid",
};
const setAsyncStorage = async (key, item) => {
  try {
    await AsyncStorage.setItem(key, item);
  } catch (error) {}
};
const getAsyncStorage = async (key) => {
  try {
    const value = await AsyncStorage.getgetItem(key);
    if (value) {
      return value;
    } else {
      return null;
    }
  } catch (error) {
    return null;
  }
};
const clearAsyncStorage = async () => {
  try {
    await AsyncStorage.clear();
  } catch (error) {}
};
export { setAsyncStorage, getAsyncStorage, clearAsyncStorage };
