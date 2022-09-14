import { configureStore } from '@reduxjs/toolkit';

import { authReducer } from './auth.slice';
import { usersReducer } from './users.slice';
import { signupReducer } from "./signup.slice";
import { channelsReducer } from "./channels.slice";
import { addChannelReducer } from "./addChannel.slice";

export * from './auth.slice';
export * from './users.slice';
export * from './signup.slice';
export * from './channels.slice';
export * from './addChannel.slice';

export const store = configureStore({
    reducer: {
        auth: authReducer,
        users: usersReducer,
        signup: signupReducer,
        channels: channelsReducer,
        addChannel: addChannelReducer
    },
});
