import {useDispatch, useSelector} from "react-redux";
import * as Yup from "yup";
import {yupResolver} from "@hookform/resolvers/yup";
import {useForm} from "react-hook-form";
import { addChannelActions } from "../_store";


export { AddChannel };

function AddChannel() {
    const dispatch = useDispatch();
    const addChannelErrors = useSelector(x => x.addChannel.error);
    const createdChannel = useSelector(x => x.addChannel.createdChannel);

    const validationSchema = Yup.object().shape({
        channelName: Yup.string().required('Channel name is required'),
        plugin: Yup.string().required('Plugin  is required'),
    });

    const formOptions = { resolver: yupResolver(validationSchema) };
    const { register, handleSubmit, formState } = useForm(formOptions);
    const { errors, isSubmitting } = formState;


    function onSubmit({ channelName, plugin }) {
        const file = document.querySelector('input[type="file"]').files[0];

        const formData = new FormData();
        formData.append("file", file, file.name);
        formData.append("channelName", channelName);

       return dispatch(addChannelActions.createChannel({ formData }));
    }

    return (
        <div className="card" style={{marginTop: 30}}>
            {createdChannel ?
                <div className="alert alert-info">
                    New channel created! (name: {createdChannel.name})
                </div>
                : null}
            <center><h4 className="card-header">Create new channel</h4></center>
            <div className="card-body">
                <form onSubmit={handleSubmit(onSubmit)}>
                    <div className="form-group">
                        <label>Channel name</label>
                        <input name="channelName" type="text" {...register('channelName')} className={`form-control ${errors.channelName ? 'is-invalid' : ''}`} />
                        <div className="invalid-feedback">{errors.channelName?.message}</div>
                    </div>
                    <div className="form-group">
                        <label>Plugin</label>
                        <input name="plugin" type="file" {...register('plugin')} className={`form-control ${errors.plugin ? 'is-invalid' : ''}`} />
                        <div className="invalid-feedback">{errors.plugin?.message}</div>
                    </div>
                    <button disabled={isSubmitting} className="btn btn-primary">
                        {isSubmitting && <span className="spinner-border spinner-border-sm mr-1"></span>}
                        Save
                    </button>
                    {addChannelErrors &&
                        <div className="alert alert-danger mt-3 mb-0">{addChannelErrors.message}</div>
                    }
                </form>
            </div>
        </div>
    )
}
