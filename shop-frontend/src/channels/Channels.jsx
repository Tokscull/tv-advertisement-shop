import {useDispatch, useSelector} from 'react-redux';
import { channelsActions } from '_store';
import * as Yup from "yup";
import {yupResolver} from "@hookform/resolvers/yup";
import {useForm} from "react-hook-form";
import {AddChannel} from "./AddChannel";

export { Channels };

function Channels() {
    const dispatch = useDispatch();
    const channels = useSelector(x => x.channels.data);
    const channelErrors = useSelector(x => x.channels.error);

    const validationSchema = Yup.object().shape({
        duration: Yup.string().required('Duration is required'),
        repeatCount: Yup.string().required('Repeat count is required'),
    });
    const formOptions = { resolver: yupResolver(validationSchema) };
    const { register, handleSubmit, formState } = useForm(formOptions);
    const { errors, isSubmitting } = formState;

    function onSubmit({ duration, repeatCount }) {
        return dispatch(channelsActions.loadAll({ duration, repeatCount }));
    }

    return (
        <div className="col-md-6">
            <AddChannel/>
            <div className="card" style={{marginTop: 30}}>
                <center><h4 className="card-header">Load all channels and prices</h4></center>
                <div className="card-body">
                    <form onSubmit={handleSubmit(onSubmit)}>
                        <div className="form-group">
                            <label>Duration</label>
                            <input name="duration" type="number" {...register('duration')} className={`form-control ${errors.duration ? 'is-invalid' : ''}`} />
                            <div className="invalid-feedback">{errors.duration?.message}</div>
                        </div>
                        <div className="form-group">
                            <label>Repeat count</label>
                            <input name="repeatCount" type="number" {...register('repeatCount')} className={`form-control ${errors.repeatCount ? 'is-invalid' : ''}`} />
                            <div className="invalid-feedback">{errors.repeatCount?.message}</div>
                        </div>
                        <button disabled={isSubmitting} className="btn btn-primary">
                            {isSubmitting && <span className="spinner-border spinner-border-sm mr-1"></span>}
                            Load
                        </button>
                        {channelErrors &&
                            <div className="alert alert-danger mt-3 mb-0">{channelErrors.message}</div>
                        }
                    </form>
                </div>
            </div>
            {channels.length !== 0 &&
                <div style={{marginTop: 30}}>
                    <h3>List of all channels and prices:</h3>
                    <ul>
                        {channels.map(ch =>
                            <li key={ch.id}><b>channelName:</b> {ch.channelName}, <b>price</b> {ch.price}</li>
                        )}
                    </ul>
                </div>
            }
        </div>
    );
}
