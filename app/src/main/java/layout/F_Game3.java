package layout;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TableLayout;

import java.util.Random;

import csim.csimemotions.Config;
import csim.csimemotions.Generic_Game;
import csim.csimemotions.R;
import csim.csimemotions.stageResults;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link F_Game3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link F_Game3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class F_Game3 extends Generic_Game {
    private boolean isGameExecuting;
    private ImageButton selectedButton;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    protected class Triple{
        ImageButton ib;
        ObjectAnimator oa1,oa2;


    }

    private OnFragmentInteractionListener mListener;
    private Triple soundButtons[];
    private FrameLayout soundLayout;
    private View.OnClickListener ocl;

    public F_Game3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     *
     * @return A new instance of fragment F_Game3.
     */
    // TODO: Rename and change types and number of parameters
    public static F_Game3 newInstance() {
        F_Game3 fragment = new F_Game3();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.soundButtons = new Triple[15];

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.isGameExecuting = true;
        this.soundLayout =  (FrameLayout) super.actividadPrincipal.findViewById(R.id.Game3_flSongTab);
        int id = new Random().nextInt();
        android.view.ViewGroup.LayoutParams  params;

        this.ocl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton b = ((ImageButton)v);
                b.setBackgroundResource(R.mipmap.ic_game_1_easy);
                F_Game3.this.selectedButton = b;
            }
        };

        for(byte contador = 0; contador < this.soundButtons.length; ++contador){
            ImageButton ib = new ImageButton(getActivity());
            ib.setBackgroundResource(R.mipmap.ic_play);
            params = new android.view.ViewGroup.LayoutParams (FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
            ib.setLayoutParams(params);

            this.soundLayout.addView(ib);

            this.soundButtons[contador] = new Triple();
            this.soundButtons[contador].ib = ib;
            this.soundButtons[contador].ib.setTag(id);
            this.soundButtons[contador].ib.setOnClickListener(this.ocl);
            ++id;
        }

        trasladarObjeto();
    }

    private void trasladarObjeto() {
        final Random r = new Random();
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(2000);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for(Triple ibt : F_Game3.this.soundButtons) {
                                    int height = F_Game3.this.soundLayout.getHeight();
                                    int width = F_Game3.this.soundLayout.getWidth();
                                    int coordenadas[] = new int[2];
                                    ibt.ib.getLocationInWindow(coordenadas);
                                    ibt.ib.setTranslationY((r.nextInt(100)+coordenadas[0])%height);
                                    ibt.ib.setTranslationX((r.nextInt(100)+coordenadas[1])%width);
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_3, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public stageResults continueGame() {
        return null;
    }

    @Override
    public void procesarRespuesta(stageResults respuesta) {

    }
}
