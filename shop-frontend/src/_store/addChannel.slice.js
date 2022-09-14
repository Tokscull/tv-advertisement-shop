import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { fetchWrapper } from '_helpers';

// create slice
const name = 'addChannel';
const initialState = createInitialState();
const extraActions = createExtraActions();
const extraReducers = createExtraReducers();
const slice = createSlice({ name, initialState, extraReducers });

// exports
export const addChannelActions = { ...slice.actions, ...extraActions };
export const addChannelReducer = slice.reducer;

// implementation
function createInitialState() {
    return {
        createdChannel: "",
        error: null
    }
}

function createExtraActions() {
    const baseUrl = `${process.env.REACT_APP_API_URL}/api/tv-channels`;

    return {
        createChannel: createChannel()
    };

    function createChannel() {
        return createAsyncThunk(
            `${name}/createChannel`,
            async ({ formData }) => await fetchWrapper.formDataPost(baseUrl, formData)
        );
    }
}

function createExtraReducers() {
    return {
        ...createChannel()
    };

    function createChannel() {
        var { pending, fulfilled, rejected } = extraActions.createChannel;
        return {
            [pending]: (state) => {
                state.error = null;
            },
            [fulfilled]: (state, action) => {
                state.createdChannel = action.payload;
            },
            [rejected]: (state, action) => {
                state.error = action.error;
            }
        };
    }
}
