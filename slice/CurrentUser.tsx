import { createSlice } from "@reduxjs/toolkit";
import dataUser from "../data/dataUser";

// var data:dataItem
const data: dataUser = {};
const slice = createSlice({
  name: "CurrentUser",
  initialState: {},

  reducers: {
    CurrentUser: (state, action) => {
      state = {
        ...action.payload,
      };
      return state;
    },
  },
});
const { reducer, actions } = slice;
export const { CurrentUser } = actions;
export default reducer;
