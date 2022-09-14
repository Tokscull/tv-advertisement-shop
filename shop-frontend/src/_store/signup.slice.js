import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';

import {fetchWrapper, history} from '_helpers';

// create slice
const name = 'signup';
const initialState = createInitialState();
const extraActions = createExtraActions();
const extraReducers = createExtraReducers();
const slice = createSlice({ name, initialState, extraReducers });

// exports
export const signupAction = { ...slice.actions, ...extraActions };
export const signupReducer = slice.reducer;

// implementation
function createInitialState() {
    return {
        createdUser: "",
        error: null
    }
}

function createExtraActions() {
    const baseUrl = `${process.env.REACT_APP_API_URL}/api/auth`;

    return {
        signup: signup()
    };

    function signup() {
        return createAsyncThunk(
            `${name}/signup`,
            async ({ username, email, password }) => await fetchWrapper.post(`${baseUrl}/signup`, { username, email, password })
        );
    }
}

function createExtraReducers() {
    return {
        ...signup()
    };

    function signup() {
        var { pending, fulfilled, rejected } = extraActions.signup;
        return {
            [pending]: (state) => {
                state.error = null;
            },
            [fulfilled]: (state, action) => {
                state.createdUser = action.payload;

                const { from } = history.location.state || { from: { pathname: '/login' } };
                history.navigate(from);
            },
            [rejected]: (state, action) => {
                state.error = action.error;
            }
        };
    }
}
