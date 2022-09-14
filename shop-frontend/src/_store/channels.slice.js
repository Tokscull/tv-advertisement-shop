import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { fetchWrapper } from '_helpers';

// create slice
const name = 'channels';
const initialState = createInitialState();
const extraActions = createExtraActions();
const extraReducers = createExtraReducers();
const slice = createSlice({ name, initialState, extraReducers });

// exports
export const channelsActions = { ...slice.actions, ...extraActions };
export const channelsReducer = slice.reducer;

// implementation
function createInitialState() {
    return {
        data: [],
        error: null
    }
}

function createExtraActions() {
    const baseUrl = `${process.env.REACT_APP_API_URL}/api/tv-channels/advertisement`;

    return {
        loadAll: loadAll()
    };

    function loadAll() {
        return createAsyncThunk(
            `${name}/getAll`,
            async ({ duration, repeatCount }) => await fetchWrapper.post(baseUrl, { duration, repeatCount })
        );
    }
}

function createExtraReducers() {
    return {
        ...loadAll()
    };

    function loadAll() {
        var { pending, fulfilled, rejected } = extraActions.loadAll;
        return {
            [pending]: (state) => {
                state.error = null;
            },
            [fulfilled]: (state, action) => {
                state.data = action.payload;
            },
            [rejected]: (state, action) => {
                state.error = action.error;
            }
        };
    }
}
