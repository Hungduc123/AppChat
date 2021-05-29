import { configureStore } from "@reduxjs/toolkit";
import chooseItemReducer from "./slice/chooseItem";
import CurrentUserReducer from "./slice/CurrentUser";

const rootReducer = {
  chooseItem: chooseItemReducer,
  CurrentUser: CurrentUserReducer,
};
const store = configureStore({
  reducer: rootReducer,
});
export default store;
