import { createSlice } from "@reduxjs/toolkit";
import dataUser from "../data/dataUser";

// var data:dataItem
const data: dataUser = {};
const slice = createSlice({
  name: "chooseItem",
  initialState: data,

  reducers: {
    chooseItem: (state, action) => {
      state = {
        ...action.payload,
      };
      return state;
    },
  },
});
const { reducer, actions } = slice;
export const { chooseItem } = actions;
export default reducer;
