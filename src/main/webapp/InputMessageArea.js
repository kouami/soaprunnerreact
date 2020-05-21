import React, {Component} from 'react';

class InputMessageArea extends Component {
    render() {
        return(
            <div className="form-group">
                <label htmlFor="request">Request</label>
                <textarea className="form-control" id="request" rows="10" cols="20" name='messageRequest'></textarea>
            </div>
        );
    }
}

export default InputMessageArea;